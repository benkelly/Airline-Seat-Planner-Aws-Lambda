package ie.benjamin.airlineseatplanner.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;

class PayloadResponseTest {
    PayloadResponse payloadResponse = new PayloadResponse("message");

    @Test
    void testSetMessage() {
        payloadResponse.setMessage("message");
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(true, payloadResponse.equals(new PayloadResponse("message")));
    }

    @Test
    void testCanEqual() {
        Assertions.assertEquals(true, payloadResponse.canEqual(new PayloadResponse("message")));
        Assertions.assertEquals(true, payloadResponse.canEqual(new PayloadResponse("m")));
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(new PayloadResponse("message").hashCode(), payloadResponse.hashCode());
    }

    @Test
    void testToString() {
        String result = payloadResponse.toString();
        Assert.assertThat(result, containsString("message"));
    }
}