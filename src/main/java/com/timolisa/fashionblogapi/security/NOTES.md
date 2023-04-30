*AUTHORIZATION FLOW USING JWT*

1. The `JwtAuthorizationFilter` class intercepts each incoming Http request to access resources, and extracts the token from the Authorization header of the request.
2. It then uses the token to authenticate the user by calling the `authenticate()` method of the `AuthenticationManager` class, which in turn calls the `authenticate()` method of the `AuthenticationProvider` class, which validates the token and extracts the user details from it.
3. Once the user is successfully authenticated, the `JwtAuthorizationFilter` class calls the `doFilterInternal()` method of the OncePerRequestFilter class, which passes the request and response objects to the next filter in the chain.
4. The next filter in the chain is the `UsernamePasswordAuthenticationFilter` class, which extracts the username and password from the request and creates an instance of the UsernamePasswordAuthenticationToken class.
5. The `UsernamePasswordAuthenticationFilter`class then passes the authentication token to the AuthenticationManager which calls the `authenticate()` method of the `AuthenticationProvider` class to validate the username and password.
6. Once the user is successfully authenticated, the `UsernamePasswordAuthenticationFilter` class creates a new instance of the `SecurityContextHolder` class and sets the authentication object in it.
7. Then Spring Security framework then forwards the authenticated request to the appropriate endpoint in the controlled based on the URL of the request.