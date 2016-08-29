package Pack;

public class ValidUserException extends Throwable
{
    @Override
    public void printStackTrace()
    {
        super.printStackTrace();
    }

    @Override
    public String getMessage()
    {
        return "INVALID USER";
    }
}
class UserAlreadyExistsException extends ValidUserException
{
    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String getMessage() {
        return "User already Exists";
    }
}
class UserNotFoundException extends ValidUserException
{
    @Override
    public String getMessage() {
        return "User not found ";
    }
}