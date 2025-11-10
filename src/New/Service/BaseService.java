package New.Service;

import java.sql.Connection;
import java.util.Scanner;

public abstract class BaseService {
    protected Connection connection;
    protected Scanner scanner;
    protected boolean exitToMainMenu = false;

    public BaseService(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public abstract void start();
    
    public boolean shouldExitToMainMenu() { 
        return exitToMainMenu;
    }
    
    public void resetExitFlag() { 
        exitToMainMenu = false;
    }
}
