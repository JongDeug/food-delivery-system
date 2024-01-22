plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.mybatis:mybatis:3.5.6")
    implementation("org.projectlombok:lombok:1.18.24")
}

tasks.test {
    useJUnitPlatform()
}