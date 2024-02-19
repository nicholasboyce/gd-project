package server.gdproject.Registration;


public record RegistrationRequest(
    String company, 
    String businessType, 
    String firstName, 
    String lastName, 
    String email,
    String password, 
    String address, 
    String city, 
    int zip, 
    String country, 
    String home, 
    String mobile, 
    String job
) {
}
