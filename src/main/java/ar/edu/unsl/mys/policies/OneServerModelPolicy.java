package ar.edu.unsl.mys.policies;
import java.util.List;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.entities.*;

public class OneServerModelPolicy implements ServerSelectionPolicy
{
    private static OneServerModelPolicy policy;


    private OneServerModelPolicy()
    {
        // Empty constructor
    }

    public static OneServerModelPolicy getInstance()
    {
        if(OneServerModelPolicy.policy == null)
            OneServerModelPolicy.policy = new OneServerModelPolicy();
        
        return OneServerModelPolicy.policy;
    }

    @Override
    public Server selectServer(List<Server> servers,Entity entity)//entity para el multiserver
    { 
        return servers.get(0);
    }
}