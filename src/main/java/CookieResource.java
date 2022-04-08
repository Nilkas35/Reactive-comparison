import io.quarkus.panache.common.Sort;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/cookies")
public class CookieResource {
    @GET
    public List<Cookie> getCookies() {
        return Cookie.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Cookie getCookie(@PathParam("id") Long id) {
        return Cookie.findById(id);
    }

    @POST
    @Transactional
    public Response createCookie(Cookie cookie) {
        cookie.persist();
        return Response.created(URI.create("/cookies/" + cookie.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Cookie updateCookie(@PathParam("id") Long id, Cookie cookie) {
        Cookie existing = Cookie.findById(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        existing.name = cookie.name;
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteCookie(@PathParam("id") Long id) {
        Cookie existing = Cookie.findById(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        existing.delete();
    }
}
