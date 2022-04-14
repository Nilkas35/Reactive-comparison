import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.tuples.Tuple2;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/drinks")
public class DrinkResource {
    @GET
    public List<Drink> getDrinks() {
        return Drink.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Drink getDrink(@PathParam("id") Long id) {
        return Drink.findById(id);
    }

    @POST
    @Transactional
    public Response createDrink(Drink drink) {
        drink.persist();
        return Response.created(URI.create("/drinks/" + drink.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Drink updateDrink(@PathParam("id") Long id, Drink drink) {
        Drink existing = Drink.findById(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        existing.name = drink.name;
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteDrink(@PathParam("id") Long id) {
        Drink existing = Drink.findById(id);
        if (existing == null) {
            throw new NotFoundException();
        }
        existing.delete();
    }

    @GET
    @Path("/{idone}/{idtwo}")
    @Transactional
    public Response getTwoItem(@PathParam("idone") Long idone, @PathParam("idtwo") Long idtwo) {
        Drink drink = Drink.findById(idone);
        Cookie cookie = Cookie.findById(idtwo);
        if (drink == null || cookie == null) {
            throw new NotFoundException();
        }

        return Response.ok(Tuple2.of(drink, cookie)).build();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GET
    @Path("/random/{max}")
    @Transactional
    public Drink getRandom(@PathParam("max") int max) {
        Long id = new Long(getRandomNumber(1, max));
        return Drink.findById(id);
    }

}
