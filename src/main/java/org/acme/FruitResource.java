package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import org.jboss.resteasy.reactive.RestPath;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

@Path("/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitResource {

    public FruitResource() {
    }

    @GET
    public Uni<List<Fruit>> get() {
        return Fruit.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(@RestPath Long id) {
        return Fruit.findById(id);
    }

    @GET
    @Path("/{idone}/{idtwo}")
    public Uni<Response> getBoth(@RestPath Long idone, @RestPath Long idtwo) {
        Uni<Fruit> fruits = Fruit.findById(idone);
        Uni<Candy> candies = Candy.findById(idtwo);
        Uni<Tuple2<Fruit, Candy>> response = Uni.combine().all().unis(fruits, candies).asTuple();

        return Panache.withTransaction(() -> response)
                .onItem().ifNotNull().transform(tuple -> Response.ok(tuple).build());
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GET
    @Path("/random/{max}")
    public Uni<Fruit> getRandom(@RestPath Integer max) {
        Long id = new Long(getRandomNumber(1, max));
        return Fruit.findById(id);

    }


    @POST
    public Uni<Fruit> create(Fruit fruit) {
        if (fruit == null || fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return Panache.withTransaction(() -> fruit.persistAndFlush());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Long id, Fruit fruit) {
        if (fruit == null || fruit.name == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return Panache
                .withTransaction(() -> Fruit.<Fruit> findById(id)
                        .onItem().ifNotNull().invoke(entity -> {entity.name = fruit.name;
                        entity.persist();})
                )
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@RestPath Long id) {
        return Panache.withTransaction(() -> Fruit.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(NO_CONTENT).build()
                        : Response.ok().status(NOT_FOUND).build());
    }

}
