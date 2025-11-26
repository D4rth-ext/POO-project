import controller.TagController;
import model.Tag;

public class Main {
    public static void main(String[] args) {
        TagController controller = new TagController();

        controller.cadastrarTag("Música");
        controller.cadastrarTag("Esportes");

        System.out.println("=== Todas as Tags ===");
        for (Tag t : controller.listarTodas()) {
            System.out.println(t.getId() + " - " + t.getNome());
        }

        controller.atualizarTag(1, "Música e Shows");
        controller.deletarTag(2);
    }
}