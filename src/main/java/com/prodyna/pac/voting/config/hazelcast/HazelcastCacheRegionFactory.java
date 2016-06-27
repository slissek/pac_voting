package com.prodyna.pac.voting.config.hazelcast;

import java.util.Properties;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.hibernate.HazelcastTimestamper;
import com.hazelcast.hibernate.local.CleanupService;
import com.hazelcast.hibernate.local.LocalRegionCache;
import com.hazelcast.hibernate.local.TimestampsRegionCache;
import com.hazelcast.hibernate.region.HazelcastCollectionRegion;
import com.hazelcast.hibernate.region.HazelcastEntityRegion;
import com.hazelcast.hibernate.region.HazelcastNaturalIdRegion;
import com.hazelcast.hibernate.region.HazelcastQueryResultsRegion;
import com.hazelcast.hibernate.region.HazelcastTimestampsRegion;
import com.prodyna.pac.voting.config.CacheConfiguration;

public class HazelcastCacheRegionFactory implements RegionFactory {

    private static final long serialVersionUID = 1L;

    private final Logger log = LoggerFactory.getLogger(HazelcastCacheRegionFactory.class);

    private final HazelcastInstance hazelcastInstance;

    private CleanupService cleanupService;

    public HazelcastCacheRegionFactory() {
        super();
        this.hazelcastInstance = CacheConfiguration.getHazelcastInstance();
    }

    /**
     * @return true - for a large cluster, unnecessary puts will most likely slow things down.
     */
    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public final QueryResultsRegion buildQueryResultsRegion(final String regionName, final Properties properties)
            throws CacheException {

        return new HazelcastQueryResultsRegion(this.hazelcastInstance, regionName, properties);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(final String regionName, final Properties properties, final CacheDataDescription metadata)
            throws CacheException {

        return new HazelcastNaturalIdRegion(this.hazelcastInstance, regionName, properties, metadata);
    }

    @Override
    public CollectionRegion buildCollectionRegion(final String regionName, final Properties properties,
            final CacheDataDescription metadata) throws CacheException {

        final HazelcastCollectionRegion<LocalRegionCache> region = new HazelcastCollectionRegion<>(this.hazelcastInstance,
                regionName, properties, metadata, new LocalRegionCache(regionName, this.hazelcastInstance, metadata));

        this.cleanupService.registerCache(region.getCache());
        return region;
    }

    @Override
    public EntityRegion buildEntityRegion(final String regionName, final Properties properties,
            final CacheDataDescription metadata) throws CacheException {

        final HazelcastEntityRegion<LocalRegionCache> region = new HazelcastEntityRegion<>(this.hazelcastInstance,
                regionName, properties, metadata, new LocalRegionCache(regionName, this.hazelcastInstance, metadata));

        this.cleanupService.registerCache(region.getCache());
        return region;
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(final String regionName, final Properties properties)
            throws CacheException {
        return new HazelcastTimestampsRegion<>(this.hazelcastInstance, regionName, properties,
                new TimestampsRegionCache(regionName, this.hazelcastInstance));
    }

    @Override
    public void start(final Settings settings, final Properties properties) throws CacheException {
        // Do nothing the hazelcast hazelcastInstance is injected
        this.log.info("Starting up {}", this.getClass().getSimpleName());

        if (this.hazelcastInstance == null) {
            throw new IllegalArgumentException("Hazelcast hazelcastInstance must not be null");
        }
        this.cleanupService = new CleanupService(this.hazelcastInstance.getName());
    }

    @Override
    public void stop() {
        // Do nothing the hazelcast instance is managed globally
        this.log.info("Shutting down {}", this.getClass().getSimpleName());
        this.cleanupService.stop();
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    @Override
    public long nextTimestamp() {
        return HazelcastTimestamper.nextTimestamp(this.hazelcastInstance);
    }
}
