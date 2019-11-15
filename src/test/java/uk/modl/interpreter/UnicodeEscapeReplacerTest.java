package uk.modl.interpreter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UnicodeEscapeReplacerTest {
    @Test
    public void can_handle_null_input() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences(null);
        assertNull(result);
    }

    @Test
    public void can_handle_empty_string_input() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("");
        assertEquals("", result);
    }

    @Test
    public void can_handle_short_non_unicode_string_input() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("abc");
        assertEquals("abc", result);
    }

    @Test
    public void can_handle_short_bad_unicode_string_input() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("\\uabc");
        assertEquals("\\uabc", result);
    }

    @Test
    public void can_handle_single_unicode_string_input_with_backslash() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("\\u2019");
        assertEquals("\u2019", result);
    }

    @Test
    public void can_handle_multiple_unicode_string_input_with_backslash() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("\\u2019\\u2019");
        assertEquals("\u2019\u2019", result);
    }

    @Test
    public void can_handle_single_unicode_string_input_with_tilde() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u2019");
        assertEquals("\u2019", result);
    }

    @Test
    public void can_handle_multiple_unicode_string_input_with_tilde() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u2019~u2019");
        assertEquals("\u2019\u2019", result);
    }

    @Test
    public void can_handle_multiple_unicode_string_input_with_mixed_tilde_and_backslash() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("\\u2019~u2019\\u2019~u2019\\u2019");
        assertEquals("\u2019\u2019\u2019\u2019\u2019", result);
    }

    @Test
    public void can_handle_escape_unicode_string_input_with_mixed_tilde_and_backslash() {
        final String result = StringEscapeReplacer.replace("\\\\u2019\\~u2019~\\u2019~~u2019\\u2019");
        assertEquals("\\u2019~u2019\\u2019~u2019\u2019", result);
    }

    @Test
    public void can_handle_a_single_escaped_unicode_string_input_with_mixed_tilde_and_backslash() {
        final String result = StringEscapeReplacer.replace("test=\\~u2019");
        assertEquals("test=~u2019", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_1() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u1f476~u1f476");
        assertEquals("👶👶", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_2() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u1f476");
        assertEquals("👶", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_3() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u1f476x");
        assertEquals("👶x", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_4() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u1f4760x");
        assertEquals("👶0x", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_5() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u0020ac");
        assertEquals("€", result);
    }

    @Test
    public void can_handle_up_to_6_characters_in_unicode_escape_sequence_6() {
        final String result = UnicodeEscapeReplacer.convertUnicodeSequences("~u0020xy");
        assertEquals(" xy", result);

    }
}