plugins {
	java
	`maven-publish`
	application
}

group = "net.sf.cssbox"
version = "1.3-SNAPSHOT"
description = "A Java Swing component that allows displaying the (X)HTML documents including the CSS support."

java {
	withSourcesJar()
	withJavadocJar()
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.bundles.swingbox.implementation)
	compileOnly(libs.xml.apis)
}

application {
	mainClass.set("org.fit.cssbox.swingbox.demo.SwingBrowser")
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])

			pom {
				name.set("SwingBox")
				description.set(project.description)
				url.set("https://cssbox.sourceforge.net/swingbox/")

				licenses {
					license {
						name.set("GNU Lesser General Public License 3.0")
						url.set("http://www.gnu.org/licenses/lgpl-3.0.txt")
					}
				}

				developers {
					developer {
						name.set("Radek Burget")
						roles.set(listOf("Main developer & project leader"))
					}
					developer {
						name.set("Peter Bielik")
						roles.set(listOf("Former developer"))
					}
				}

				contributors {
					contributor {
						name.set("Kaj Kandler")
						email.set("kajkandler@conficio.com")
						organization.set("Conficio")
						organizationUrl.set("https://conficio.com")
					}
					contributor {
						name.set("Dave Jarvis")
						organization.set("White Magic Software, Ltd.")
						organizationUrl.set("https://whitemagicsoftware.com")
					}
					contributor {
						name.set("Caroline Bell")
						email.set("caroline@halotroop.com")
						organization.set("halotroop")
						organizationUrl.set("https://web0.halotroop.com")
					}
				}

				scm {
					connection.set("scm:git:git@github.com:halotroop2288/SwingBox.git")
					developerConnection.set("scm:git:git@github.com:halotroop2288/SwingBox.git")
					url.set("https://github.com/halotroop2288/SwingBox")
					tag.set("HEAD")
				}
			}
		}
	}
}
