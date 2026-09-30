plugins {
    `java-library`
}

// Shared agent harness: loop, budgets, tool permissions, transcripts, replay (§16). Built in W8.

dependencies {
    api(project(":common"))
}
