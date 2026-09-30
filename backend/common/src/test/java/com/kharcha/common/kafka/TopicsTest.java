package com.kharcha.common.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class TopicsTest {

    /** Gradle runs tests with the module directory (backend/common) as working directory. */
    private static final Path CREATE_TOPICS = Path.of("../../infra/kafka/create-topics.sh");

    /** Matches the topic table rows in the script, e.g. {@code "raw-events 3 12 30"}. */
    private static final Pattern ROW = Pattern.compile("^([a-z-]+) +\\d+ +\\d+ +\\d+$", Pattern.MULTILINE);

    @Test
    void dltAppendsSuffix() {
        assertThat(Topics.dlt(Topics.RAW_EVENTS)).isEqualTo("raw-events.DLT");
    }

    @Test
    void topicInitScriptCreatesExactlyTheseTopics() throws IOException {
        String script = Files.readString(CREATE_TOPICS);
        Matcher m = ROW.matcher(script);
        List<String> scripted = m.results().map(r -> r.group(1)).toList();
        assertThat(scripted).containsExactlyElementsOf(Topics.ALL);
    }
}
