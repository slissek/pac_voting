package com.prodyna.pac.voting.web.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableSet;
import com.prodyna.pac.voting.domain.util.JSR310DateTimeSerializer;
import com.prodyna.pac.voting.domain.util.JSR310LocalDateDeserializer;
import com.prodyna.pac.voting.security.AuthoritiesConstants;

/**
 * Utility class for testing REST controllers.
 */
public class TestUtil
{

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    /**
     * Convert an object to JSON byte array.
     *
     * @param object
     *            the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(final Object object) throws IOException
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
        module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }

    /**
     * Create a byte array with a specific size filled with specified data.
     *
     * @param size
     *            the size of the byte array
     * @param data
     *            the data to put in the byte array
     */
    public static byte[] createByteArray(final int size, final String data)
    {
        final byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++)
        {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableSet<T> getAdminAuthorities()
    {
        final Set<SimpleGrantedAuthority> set = Collections.singleton(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        return (ImmutableSet<T>) ImmutableSet.copyOf(set);
    }
}
