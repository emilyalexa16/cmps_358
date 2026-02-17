import java.rmi.Naming;

public class EDR_Register {
    public static void main(String[] argv) {
        try {          
            Naming.rebind("EDR", new EDR_Server_Impl());
            System.out.println("EDR Server is ready.");
        } catch (Exception e) {
            System.out.println("EDR Server: " + e);
        }
    } 
}
