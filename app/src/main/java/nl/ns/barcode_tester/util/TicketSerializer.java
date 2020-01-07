package nl.ns.barcode_tester.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_2.Ticket918Dash2;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by joelhaasnoot on 02/12/2016.
 */
public class TicketSerializer implements JsonSerializer<Ticket>, JsonDeserializer<Ticket> {

    public static final String FIELD_STANDARD = "standard";
    public static final String FIELD_TICKET = "ticket";

    @Override
    public Ticket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject =  json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(FIELD_STANDARD);
        TicketStandard standard = TicketStandard.valueOf(prim.toString());

        return context.deserialize(jsonObject.get(FIELD_TICKET), standard == TicketStandard.TICKET918_2 ? Ticket918Dash2.class : Ticket918Dash3.class);
    }

    @Override
    public JsonElement serialize(Ticket src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        retValue.addProperty(FIELD_STANDARD, src.getStandard().toString());

        JsonElement elem;
        if (src.getStandard() == TicketStandard.TICKET918_2) {
            elem = context.serialize((Ticket918Dash2) src);
        } else {
            elem = context.serialize((Ticket918Dash3) src);
        }

        retValue.add(FIELD_TICKET, elem);
        return retValue;
    }
}
