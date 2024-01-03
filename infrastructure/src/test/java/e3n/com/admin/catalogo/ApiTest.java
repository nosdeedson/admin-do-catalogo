package e3n.com.admin.catalogo;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

public interface ApiTest {

    JwtRequestPostProcessor ADMIN_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"));

    JwtRequestPostProcessor CATEGORIES_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"));

    JwtRequestPostProcessor GENRES_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"));

    JwtRequestPostProcessor VIDEOS_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"));

    JwtRequestPostProcessor CAST_MEMBERS_JWT =
            jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"));
}
