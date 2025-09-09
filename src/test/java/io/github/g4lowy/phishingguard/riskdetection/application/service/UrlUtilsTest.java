package io.github.g4lowy.phishingguard.riskdetection.application.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlUtilsTest {

    @Test
    void extractUrls_shouldFindSingleHttpUrl() {
        String text = "Check this link: http://example.com/test";
        List<String> urls = UrlUtils.extractUrls(text);

        assertThat(urls).containsExactly("http://example.com/test");
    }

    @Test
    void extractUrls_shouldFindSingleHttpsUrl() {
        String text = "Go here: https://example.org/path?x=1";
        List<String> urls = UrlUtils.extractUrls(text);

        assertThat(urls).containsExactly("https://example.org/path?x=1");
    }

    @Test
    void extractUrls_shouldFindUrlWithoutScheme() {
        String text = "Visit example.net/page";
        List<String> urls = UrlUtils.extractUrls(text);

        assertThat(urls).containsExactly("example.net/page");
    }

    @Test
    void extractUrls_shouldFindMultipleUrls() {
        String text = "Here are two: http://foo.com and https://bar.org.";
        List<String> urls = UrlUtils.extractUrls(text);

        assertThat(urls).containsExactly("http://foo.com", "https://bar.org");
    }

    @Test
    void extractUrls_shouldReturnEmptyListWhenNoUrls() {
        String text = "Just plain text without any links.";
        List<String> urls = UrlUtils.extractUrls(text);

        assertThat(urls).isEmpty();
    }

    @Test
    void normalize_shouldAddHttpSchemeIfMissing() {
        String normalized = UrlUtils.normalize("example.com");

        assertThat(normalized).isEqualTo("http://example.com/");
    }

    @Test
    void normalize_shouldLowercaseHost() {
        String normalized = UrlUtils.normalize("HTTP://ExAmPlE.CoM/Path");

        assertThat(normalized).isEqualTo("http://example.com/Path");
    }

    @Test
    void normalize_shouldRemoveDefaultPort() {
        String normalized = UrlUtils.normalize("http://example.com:80/page");

        assertThat(normalized).isEqualTo("http://example.com/page");
    }

    @Test
    void normalize_shouldRemoveFragment() {
        String normalized = UrlUtils.normalize("https://example.com/page#section1");

        assertThat(normalized).isEqualTo("https://example.com/page");
    }

    @Test
    void hostOf_shouldReturnHostFromValidUrl() {
        String host = UrlUtils.hostOf("http://example.com/path");

        assertThat(host).isEqualTo("example.com");
    }

    @Test
    void hostOf_shouldReturnInputWhenInvalidUrl() {
        String host = UrlUtils.hostOf("not a valid url");

        assertThat(host).isEqualTo("not a valid url");
    }

    @Test
    void hostOf_shouldReturnNullWhenUrlHasNoHost() {
        String host = UrlUtils.hostOf("mailto:user@example.com");

        assertThat(host).isNull();
    }

    @Test
    void eTldPlusOne_shouldReturnBaseDomain() {
        String result = UrlUtils.eTldPlusOne("sub.example.co.uk");

        assertThat(result).isEqualTo("example.co.uk");
    }

    @Test
    void eTldPlusOne_shouldHandleSimpleDomain() {
        String result = UrlUtils.eTldPlusOne("example.com");

        assertThat(result).isEqualTo("example.com");
    }

    @Test
    void eTldPlusOne_shouldThrowOnInvalidDomain() {
        assertThrows(IllegalArgumentException.class,
                () -> UrlUtils.eTldPlusOne("not_a_domain"));
    }
}
