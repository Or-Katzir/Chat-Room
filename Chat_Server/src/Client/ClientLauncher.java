package Client;

public class ClientLauncher {

	public static void main(String[] args) {

		ClientModel model = new ClientModel();
		ClientView view = new ClientView();
		ClientController client = new ClientController(model, view);

	}
}
