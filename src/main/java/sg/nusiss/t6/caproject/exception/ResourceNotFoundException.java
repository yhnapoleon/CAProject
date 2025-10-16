//By Ying Hao

package sg.nusiss.t6.caproject.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceName, Object id) {
    super(resourceName + " not found with id: " + id);
  }
}
