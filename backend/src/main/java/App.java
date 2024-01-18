import com.bp3.model.Task;
import com.bp3.service.BusinessProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {
  private static Logger log = LoggerFactory.getLogger(App.class);

  public String getGreeting() {
    return "Hello world";
  }

  public static void main(String[] args) {
    log.info("Starting process...");
    new App().doProcessing();
    log.info("Ending process... Spitting out file, check the data directory for file with correct timestamp");
  }

  private void doProcessing() {
    BusinessProcessService processService = new BusinessProcessService();
    Task task = processService.readTasks();
    processService.processEdgesToNodes(task);
    processService.writeTasks(task);
  }
}
