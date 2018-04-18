package com.snapengage.intergrations.custom.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.data.couchbase.core.query.Consistency.STRONGLY_CONSISTENT;

/**
 * Couchbase Setup: The host, bucket and its password will be read from the environment variable.
 * The server will fails to start if the configured couchbase is not reachable or bucket is not accessible
 */
@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${COUCHBASE_HOST}")
    private String host;
    @Value("${COUCHBASE_BUCKET}")
    private String bucketName;
    @Value("${COUCHBASE_BUCKET_PASSWORD}")
    private String bucketPassword;

    @Bean
    public CouchbaseOperations couchbaseOperations() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(couchbaseClusterInfo(), couchbaseClient(), mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(STRONGLY_CONSISTENT);

        return template;
    }

    @Override
    protected List<String> getBootstrapHosts() {
        return singletonList(host);
    }

    @Bean
    public Cluster cluster() {
        return CouchbaseCluster.create(host);
    }

    @Bean
    public Bucket bucket() {
        Bucket bucket = cluster().openBucket(bucketName, bucketPassword);
        bucket.bucketManager().createN1qlPrimaryIndex(true, false); /* Primary index is required to perform a non-id based queries */
        return bucket;
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }

    @Override
    protected String getBucketPassword() {
        return bucketPassword;
    }

}
