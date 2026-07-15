// Neo4j Database Schema for NEXORA Knowledge Graph
// This script initializes the knowledge graph structure

// Create constraints for data integrity
CREATE CONSTRAINT project_unique IF NOT EXISTS FOR (p:Project) REQUIRE p.id IS UNIQUE;
CREATE CONSTRAINT module_unique IF NOT EXISTS FOR (m:Module) REQUIRE m.id IS UNIQUE;
CREATE CONSTRAINT class_unique IF NOT EXISTS FOR (c:Class) REQUIRE c.id IS UNIQUE;
CREATE CONSTRAINT method_unique IF NOT EXISTS FOR (m:Method) REQUIRE m.id IS UNIQUE;
CREATE CONSTRAINT api_unique IF NOT EXISTS FOR (a:API) REQUIRE a.id IS UNIQUE;
CREATE CONSTRAINT database_unique IF NOT EXISTS FOR (d:Database) REQUIRE d.id IS UNIQUE;
CREATE CONSTRAINT developer_unique IF NOT EXISTS FOR (d:Developer) REQUIRE d.email IS UNIQUE;
CREATE CONSTRAINT documentation_unique IF NOT EXISTS FOR (d:Documentation) REQUIRE d.id IS UNIQUE;
CREATE CONSTRAINT commit_unique IF NOT EXISTS FOR (c:Commit) REQUIRE c.hash IS UNIQUE;
CREATE CONSTRAINT package_unique IF NOT EXISTS FOR (p:Package) REQUIRE p.name IS UNIQUE;
CREATE CONSTRAINT interface_unique IF NOT EXISTS FOR (i:Interface) REQUIRE i.id IS UNIQUE;

// Create indexes for performance
CREATE INDEX project_name IF NOT EXISTS FOR (p:Project) ON (p.name);
CREATE INDEX module_name IF NOT EXISTS FOR (m:Module) ON (m.name);
CREATE INDEX class_name IF NOT EXISTS FOR (c:Class) ON (c.name);
CREATE INDEX method_name IF NOT EXISTS FOR (m:Method) ON (m.name);
CREATE INDEX api_endpoint IF NOT EXISTS FOR (a:API) ON (a.endpoint);
CREATE INDEX developer_name IF NOT EXISTS FOR (d:Developer) ON (d.name);
CREATE INDEX commit_date IF NOT EXISTS FOR (c:Commit) ON (c.date);

// Sample queries for creating nodes (executed separately)
// PROJECT NODE
CREATE (p:Project {id: "proj_1", name: "NEXORA", description: "Software Knowledge Continuity Framework", createdAt: datetime()})
RETURN p;

// DEVELOPER NODES
CREATE (d1:Developer {email: "dev1@nexora.com", name: "John Developer", role: "Senior Developer", expertise: ["Java", "React", "Neo4j"]}),
       (d2:Developer {email: "dev2@nexora.com", name: "Jane Architect", role: "Architect", expertise: ["Architecture", "DevOps", "Cloud"]});

// PACKAGE NODE
CREATE (pkg:Package {name: "com.nexora.core", language: "Java", files: 25});

// MODULE NODE
CREATE (mod:Module {id: "mod_1", name: "Authentication Module", description: "Handles user authentication and authorization", complexity: "Medium"});

// CLASS NODE
CREATE (cls:Class {id: "cls_1", name: "AuthenticationService", type: "Service", lines_of_code: 250, methods_count: 12});

// INTERFACE NODE
CREATE (iface:Interface {id: "iface_1", name: "SecurityProvider", methods: ["authenticate", "authorize", "validate"]});

// METHOD NODE
CREATE (meth:Method {id: "meth_1", name: "authenticate", return_type: "boolean", parameters: ["username", "password"], complexity: "Low"});

// API NODE
CREATE (api:API {id: "api_1", endpoint: "/api/auth/login", method: "POST", description: "User login endpoint", version: "1.0"});

// DATABASE NODE
CREATE (db:Database {id: "db_1", name: "PostgreSQL", type: "Relational", tables: ["users", "projects", "repositories"]});

// DOCUMENTATION NODE
CREATE (doc:Documentation {id: "doc_1", title: "Authentication Guide", content: "", url: "/docs/auth"});

// COMMIT NODE
CREATE (commit:Commit {hash: "abc123def456", message: "Initial authentication setup", author: "John Developer", date: datetime()});
