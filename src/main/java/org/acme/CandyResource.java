package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

@Path("/candy")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CandyResource {

    @GET
    public Uni<List<Candy>> get() {
        return Candy.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Uni<Candy> getSingle(@RestPath Long id) {
        return Candy.findById(id);
    }


    @POST
    public Uni<Response> create(Candy candy) {
        if (candy == null || candy.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return Panache.withTransaction(candy::persist)
                .replaceWith(Response.ok(candy).status(CREATED)::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Long id, Candy candy) {
        if (candy == null || candy.name == null) {
            throw new WebApplicationException("Candy name was not set on request.", 422);
        }
        return Panache
                .withTransaction(() -> Candy.<Candy> findById(id)
                        .onItem().ifNotNull().invoke(entity -> entity.name = candy.name)
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@RestPath Long id) {
        return Panache.withTransaction(() -> Candy.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(NO_CONTENT).build()
                        : Response.ok().status(NOT_FOUND).build());
    }
}


