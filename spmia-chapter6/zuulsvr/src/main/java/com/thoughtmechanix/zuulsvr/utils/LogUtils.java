package com.thoughtmechanix.zuulsvr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

	public static String logResponse(HttpResponse response) {
		if (response == null) {
			return "<null>";
		}
		StringBuilder sb = new StringBuilder("{");
		sb.append("status: ").append(response.getStatusLine().getStatusCode());

		String str = null;
		if (response.getEntity() != null) {
			try {
				str = EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				str = e.toString();
				logger.error("logResponse# entity", e);
			}
		}
		sb.append(", entity: ").append(str);
		sb.append("}");
		return sb.toString();
	}

	public static class IOUtils {

		private static final int EOF = -1;
		private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

		public static String toString(InputStream input, Charset encoding) throws IOException {
			StringBuilderWriter sw = new StringBuilderWriter();
			copy(input, sw, encoding);
			return sw.toString();
		}

		public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
			InputStreamReader in = new InputStreamReader(input, encoding == null ? Charset.defaultCharset() : encoding);
			copy(in, output);
		}

		public static int copy(Reader input, Writer output) throws IOException {
			long count = copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
			if (count > Integer.MAX_VALUE) {
				return -1;
			}
			return (int) count;
		}

		public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
			long count = 0;
			int n = 0;
			while (EOF != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
			}
			return count;
		}
	}

	public static class StringBuilderWriter extends Writer implements Serializable {

		private static final long serialVersionUID = 3483324146725554119L;

		private final StringBuilder builder;

		/**
		 * Construct a new {@link StringBuilder} instance with default capacity.
		 */
		public StringBuilderWriter() {
			this.builder = new StringBuilder();
		}

		/**
		 * Construct a new {@link StringBuilder} instance with the specified capacity.
		 *
		 * @param capacity
		 *            The initial capacity of the underlying {@link StringBuilder}
		 */
		public StringBuilderWriter(int capacity) {
			this.builder = new StringBuilder(capacity);
		}

		/**
		 * Construct a new instance with the specified {@link StringBuilder}.
		 *
		 * @param builder
		 *            The String builder
		 */
		public StringBuilderWriter(StringBuilder builder) {
			this.builder = builder != null ? builder : new StringBuilder();
		}

		/**
		 * Append a single character to this Writer.
		 *
		 * @param value
		 *            The character to append
		 * @return This writer instance
		 */
		@Override
		public Writer append(char value) {
			builder.append(value);
			return this;
		}

		/**
		 * Append a character sequence to this Writer.
		 *
		 * @param value
		 *            The character to append
		 * @return This writer instance
		 */
		@Override
		public Writer append(CharSequence value) {
			builder.append(value);
			return this;
		}

		/**
		 * Append a portion of a character sequence to the {@link StringBuilder}.
		 *
		 * @param value
		 *            The character to append
		 * @param start
		 *            The index of the first character
		 * @param end
		 *            The index of the last character + 1
		 * @return This writer instance
		 */
		@Override
		public Writer append(CharSequence value, int start, int end) {
			builder.append(value, start, end);
			return this;
		}

		/**
		 * Closing this writer has no effect.
		 */
		@Override
		public void close() {
		}

		/**
		 * Flushing this writer has no effect.
		 */
		@Override
		public void flush() {
		}

		/**
		 * Write a String to the {@link StringBuilder}.
		 * 
		 * @param value
		 *            The value to write
		 */
		@Override
		public void write(String value) {
			if (value != null) {
				builder.append(value);
			}
		}

		/**
		 * Write a portion of a character array to the {@link StringBuilder}.
		 *
		 * @param value
		 *            The value to write
		 * @param offset
		 *            The index of the first character
		 * @param length
		 *            The number of characters to write
		 */
		@Override
		public void write(char[] value, int offset, int length) {
			if (value != null) {
				builder.append(value, offset, length);
			}
		}

		/**
		 * Return the underlying builder.
		 *
		 * @return The underlying builder
		 */
		public StringBuilder getBuilder() {
			return builder;
		}

		/**
		 * Returns {@link StringBuilder#toString()}.
		 *
		 * @return The contents of the String builder.
		 */
		@Override
		public String toString() {
			return builder.toString();
		}
	}

}
