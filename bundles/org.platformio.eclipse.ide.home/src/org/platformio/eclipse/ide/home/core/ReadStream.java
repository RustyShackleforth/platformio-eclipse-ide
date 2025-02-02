/*******************************************************************************
 * Copyright (c) 2021 PlatformIO and ArSysOp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     Nikifor Fedorov (ArSysOp) - initial API and implementation
 *******************************************************************************/
package org.platformio.eclipse.ide.home.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Consumer;

public final class ReadStream extends Thread {
	private final InputStream stream;
	private final Consumer<String> consumer;
	private final Consumer<Exception> handler;

	public ReadStream(InputStream input) {
		this(input, System.out::println, t -> t.printStackTrace());
	}

	@SuppressWarnings("resource")
	public ReadStream(InputStream input, Consumer<String> consumer, Consumer<Exception> handler) {
		Objects.requireNonNull(input, "ReadStream::stream"); //$NON-NLS-1$
		Objects.requireNonNull(consumer, "ReadStream::consumer"); //$NON-NLS-1$
		Objects.requireNonNull(handler, "ReadStream::handler"); //$NON-NLS-1$
		this.stream = input;
		this.consumer = consumer;
		this.handler = handler;
	}

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			while ((line = br.readLine()) != null) {
				consumer.accept(line);
			}
		} catch (final IOException e) {
			handler.accept(e);
		}
	}
}
