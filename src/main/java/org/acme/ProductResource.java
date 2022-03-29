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
import static javax.ws.rs.core.Response.Status.NOT_FOUND;


@Path("/products")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ProductResource {
    @GET
    public Uni<List<Product>> get() {
        return Product.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Uni<Product> getSingle(@RestPath Long id) {
        return Product.findById(id);
    }

    @POST
    public Uni<Response> create(Product product) {
        if (product == null || product.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Panache.withTransaction(product::persist)
                .replaceWith(Response.ok(product).status(CREATED)::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Long id, Product product) {
        if (product == null || product.name == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return Panache
                .withTransaction(() -> Product.<Product> findById(id)
                        .onItem().ifNotNull().invoke(entity -> entity.name = product.name)
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@RestPath Long id) {
        return Panache.withTransaction(() -> Product.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(NO_CONTENT).build()
                        : Response.ok().status(NOT_FOUND).build());
    }

}
