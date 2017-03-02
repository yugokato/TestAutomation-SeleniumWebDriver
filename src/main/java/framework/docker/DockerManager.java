package framework.docker;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.messages.AttachedNetwork;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;

public class DockerManager {
	private static final Logger logger = Logger.getLogger(DockerManager.class);
	private static DockerClient docker;
	private static final String DOCKER_HOST = "192.168.99.100:2376";
	private static Map<ContainerCreation, Map<String, String>> containersMap;

	public static DockerClient getDockerClient(){
		try{
        	docker = DefaultDockerClient.builder().uri("https://" + DOCKER_HOST)
        			.dockerCertificates(new DockerCertificates(Paths.get("/Users/yugokato/.docker/machine/machines/default")))
                    .build();
        }catch(DockerCertificateException e){
        	e.printStackTrace();
        }
		return docker;
	}
	
	public static void startTestContainers(final String[] INITIAL_CONTAINERS) throws Exception{
    	List<Container> containers = docker.listContainers(ListContainersParam.allContainers());
        for(Container container : containers) {
        	for( String name : container.names()){
        		if (! Arrays.asList(INITIAL_CONTAINERS).contains(name)){
        			if (container.state().equals("running")){
        				docker.killContainer(container.id());
        			}
            		docker.removeContainer(container.id());
            		logger.info("Removed a container: " + name);
        		}
        		else if (! container.state().equals("running")){
            		docker.startContainer(container.id());
            		logger.info("Started a container: " + name);
            	}
            }
        }
    }
	
	public static Map<String, String> createAndStartContainer(Map<String, String> testConfigMap) throws Exception{
		String containerName = testConfigMap.get("CONTAINER_NAME");
		String networkName = testConfigMap.get("NETWORK_NAME");
		String containerIP;
		
		ContainerConfig config = ContainerConfig.builder()
    		    .image(testConfigMap.get("IMAGE"))
    		    .hostname(testConfigMap.get("HOSTNAME"))
    		    .openStdin(true)
    		    .tty(true)
    		    .build();
		
		// create a new container
    	ContainerCreation container = docker.createContainer(config, containerName);
    	
    	// attach the container to the test network
    	docker.connectToNetwork(container.id(), networkName);
    	
    	// start the container
    	docker.startContainer(container.id());
    	
    	// get container id and its assigned IP Address
    	containersMap = new HashMap<ContainerCreation, Map<String, String>>();
    	Map<String, String> containerInfo = new HashMap<>();
    	containerIP = getContainerIP(container, networkName);
    	containerInfo.put("ID", container.id());
    	containerInfo.put("IP_ADDRESS", containerIP);
    	
    	containersMap.put(container, containerInfo);
    	
    	logger.info(String.format("Created and started a new container: {container name: %s, IP Address: %s}", containerName, containerIP));
    	
    	return containersMap.get(container);

	}
	
	private static String getContainerIP(ContainerCreation container, String networkName) throws Exception{
		ContainerInfo containerInfo = docker.inspectContainer(container.id());
    	AttachedNetwork attachedNetwork = containerInfo.networkSettings().networks().get(networkName);
    	String containerIP = attachedNetwork.ipAddress();
    	return containerIP;
	}
}
