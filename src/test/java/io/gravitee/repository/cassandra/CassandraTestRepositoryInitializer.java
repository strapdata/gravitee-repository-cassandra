/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.repository.cassandra;

import com.datastax.driver.core.Session;
import io.gravitee.repository.config.TestRepositoryInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Azize ELAMRANI (azize.elamrani at graviteesource.com)
 * @author Adel Abdelhak (adel.abdelhak@leansys.fr)
 */
public class CassandraTestRepositoryInitializer implements TestRepositoryInitializer {

    private final Logger LOGGER = LoggerFactory.getLogger(CassandraTestRepositoryInitializer.class);

    @Autowired
    private Session session;

    @Override
    public void setUp() {
        LOGGER.debug("Starting tests");

        session.execute("CREATE KEYSPACE IF NOT EXISTS gravitee WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.tenants (id text PRIMARY KEY, name text, description text);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.views (id text PRIMARY KEY, name text, description text, hidden boolean, defaultView boolean, view_order int, updated_at timestamp, created_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.tags (id text PRIMARY KEY, name text, description text);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.apikeys (key text PRIMARY KEY, subscription text, application text, plan text, expire_at timestamp, created_at timestamp, updated_at timestamp, revoked_at timestamp, revoked boolean);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.apis (id text PRIMARY KEY, name text, description text, version text, definition text, deployed_at timestamp, created_at timestamp, updated_at timestamp, visibility text, lifecycle_state text, picture text, groups set<text>, views set<text>, labels list<text>);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.applications (id text PRIMARY KEY, name text, description text, type text, created_at timestamp, updated_at timestamp, groups set<text>, status text);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.events (id text PRIMARY KEY, type text, payload text, parent_id text, properties map<text, text>, created_at timestamp, updated_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.groups (id text PRIMARY KEY, name text, administrators list<text>, event_rules list<text>, created_at timestamp, updated_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.memberships (user_id text, reference_id text, reference_type text, roles set<text>, created_at timestamp, updated_at timestamp, PRIMARY KEY (user_id, reference_id, reference_type));");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.pages (id text PRIMARY KEY, name text, type text, content text, last_contributor text, page_order int, published boolean, source_type text, source_configuration text, configuration_tryiturl text, configuration_tryit boolean, api text, excludedGroups list<text>, created_at timestamp, updated_at timestamp, homepage boolean);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.plans (id text PRIMARY KEY, name text, description text, validation text, type text, status text, plan_order int, apis set<text>, created_at timestamp, updated_at timestamp, definition text, characteristics list<text>, security text, excludedGroups list<text>, published_at timestamp, closed_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.users (username text PRIMARY KEY, source text, source_id text, password text, email text, firstname text, lastname text, roles set<text>, created_at timestamp, updated_at timestamp, last_connection_at timestamp, picture text);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.subscriptions (id text PRIMARY KEY, plan text, application text, reason text, status text, created_at timestamp, updated_at timestamp, processed_at timestamp, starting_at timestamp, ending_at timestamp, processed_by text, subscribed_by text, closed_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.metadata (key text, reference_id text, reference_type text, name text, format text, value text, created_at timestamp, updated_at timestamp, PRIMARY KEY (key, reference_id, reference_type));");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.roles (scope int, name text, description text, is_default boolean, system boolean, permissions list<int>, created_at timestamp, updated_at timestamp, PRIMARY KEY (scope, name));");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.ratings (id text PRIMARY KEY, api text, user text, rate tinyint, title text, comment text, created_at timestamp, updated_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.ratinganswers (id text PRIMARY KEY, rating text, comment text, user text, created_at timestamp, updated_at timestamp);");
        session.execute("CREATE TABLE IF NOT EXISTS gravitee.audits (id text PRIMARY KEY, referenceId text, referenceType text, username text, createdAt timestamp, event text, patch text, properties map<text, text>);");

        session.execute("CREATE TABLE IF NOT EXISTS gravitee.ratelimits (id text PRIMARY KEY, last_request timestamp, counter bigint, reset_time timestamp, created_at timestamp, updated_at timestamp, async boolean);");
    }

    @Override
    public void tearDown() {
        LOGGER.debug("Ending tests");
        // drop keyspace takes too much time
        // session.execute("DROP KEYSPACE IF EXISTS gravitee;");
        session.execute("TRUNCATE gravitee.tenants;");
        session.execute("TRUNCATE gravitee.views;");
        session.execute("TRUNCATE gravitee.tags;");
        session.execute("TRUNCATE gravitee.apikeys;");
        session.execute("TRUNCATE gravitee.apis;");
        session.execute("TRUNCATE gravitee.applications;");
        session.execute("TRUNCATE gravitee.events;");
        session.execute("TRUNCATE gravitee.groups;");
        session.execute("TRUNCATE gravitee.memberships;");
        session.execute("TRUNCATE gravitee.pages;");
        session.execute("TRUNCATE gravitee.plans;");
        session.execute("TRUNCATE gravitee.users;");
        session.execute("TRUNCATE gravitee.subscriptions;");
        session.execute("TRUNCATE gravitee.metadata;");
        session.execute("TRUNCATE gravitee.roles");
        session.execute("TRUNCATE gravitee.ratings");
        session.execute("TRUNCATE gravitee.ratinganswers");
        session.execute("TRUNCATE gravitee.audits");

        session.execute("TRUNCATE gravitee.ratelimits;");
    }
}
