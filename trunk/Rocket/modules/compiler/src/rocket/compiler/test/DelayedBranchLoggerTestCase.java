/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.compiler.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import rocket.compiler.TreeLoggers;

import com.google.gwt.core.ext.TreeLogger;

public class DelayedBranchLoggerTestCase extends TestCase {

	public void testBranch() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());
	}

	public void testBranchThenLeafLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals(0, messages.size());

		branch.log(TreeLogger.DEBUG, "2", null);
		assertEquals("" + messages, 2, messages.size());
		assertEquals(".1", messages.get(0));
		assertEquals("..2", messages.get(1));
	}

	public void testDelayedBranchThenRegularBranchLeafLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger delayedBranch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals(0, messages.size());

		final TreeLogger branchBranch = delayedBranch.branch(TreeLogger.DEBUG, "2", null);

		branchBranch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 3, messages.size());
		assertEquals(".1", messages.get(0));
		assertEquals("..2", messages.get(1));
		assertEquals("...3", messages.get(2));
	}

	public void testTwoBranchesThenLeafLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals(0, messages.size());

		final TreeLogger branchOfBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		branchOfBranch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 3, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..2", messages.get(1));
		assertEquals("" + messages, "...3", messages.get(2));
	}

	public void testTwoBranchesThenBranchLogSecondBranchAttemptIsLost() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null); // lost
		assertEquals("" + messages, 0, messages.size());

		branch.log(TreeLogger.DEBUG, "3", null);

		assertEquals("" + messages, 2, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..3", messages.get(1));
	}

	public void testTwoBranchesThenBranchLogThenBranchBranchLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		branch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 2, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..3", messages.get(1));

		branchBranch.log(TreeLogger.DEBUG, "4", null);

		assertEquals("" + messages, 4, messages.size());
		assertEquals("" + messages, "..2", messages.get(2));
		assertEquals("" + messages, "...4", messages.get(3));
	}

	public void testThreeBranchesThenOneLeafLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranchBranch = TreeLoggers.delayedBranch(branchBranch, TreeLogger.DEBUG, "3", null, null);
		assertEquals("" + messages, 0, messages.size());

		branchBranchBranch.log(TreeLogger.DEBUG, "4", null);
		assertEquals("" + messages, 4, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..2", messages.get(1));
		assertEquals("" + messages, "...3", messages.get(2));
		assertEquals("" + messages, "....4", messages.get(3));
	}

	public void testThreeBranchesThenOneLeafLogWithLostSecondLevelBranch() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger lostBranchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger lostBranchBranchBranch = TreeLoggers.delayedBranch(lostBranchBranch, TreeLogger.DEBUG, "3", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "4", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranchBranch = TreeLoggers.delayedBranch(branchBranch, TreeLogger.DEBUG, "5", null, null);
		assertEquals("" + messages, 0, messages.size());

		branchBranchBranch.log(TreeLogger.DEBUG, "6", null);

		assertEquals("" + messages, 4, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..4", messages.get(1));
		assertEquals("" + messages, "...5", messages.get(2));
		assertEquals("" + messages, "....6", messages.get(3));
	}

	public void testThreeBranchesThenTwoLeafLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranchBranch = TreeLoggers.delayedBranch(branchBranch, TreeLogger.DEBUG, "3", null, null);
		assertEquals("" + messages, 0, messages.size());

		branchBranchBranch.log(TreeLogger.DEBUG, "4", null);
		branchBranchBranch.log(TreeLogger.DEBUG, "5", null);

		assertEquals("" + messages, 5, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..2", messages.get(1));
		assertEquals("" + messages, "...3", messages.get(2));
		assertEquals("" + messages, "....4", messages.get(3));
		assertEquals("" + messages, "....5", messages.get(4));
	}

	public void testTwoBranchesThenBranchLogThenBranchBranchLogThenBranchLog() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		branch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 2, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..3", messages.get(1));

		branchBranch.log(TreeLogger.DEBUG, "4", null);

		assertEquals("" + messages, 4, messages.size());
		assertEquals("" + messages, "..2", messages.get(2));
		assertEquals("" + messages, "...4", messages.get(3));

		branch.log(TreeLogger.DEBUG, "5", null);

		assertEquals("" + messages, 5, messages.size());
		assertEquals("" + messages, "..5", messages.get(4));
	}

	public void testTwoBranchesThenTwiceBranchLogThenBranchBranchLogs() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger branch = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger branchBranch = TreeLoggers.delayedBranch(branch, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		branch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 2, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..3", messages.get(1));

		branchBranch.log(TreeLogger.DEBUG, "4", null);

		assertEquals("" + messages, 4, messages.size());
		assertEquals("" + messages, "..2", messages.get(2));
		assertEquals("" + messages, "...4", messages.get(3));

		branch.log(TreeLogger.DEBUG, "5", null);

		assertEquals("" + messages, 5, messages.size());
		assertEquals("" + messages, "..5", messages.get(4));

		branchBranch.log(TreeLogger.DEBUG, "6", null);

		assertEquals("" + messages, 6, messages.size());
		assertEquals("" + messages, "...6", messages.get(5));
	}

	public void testTwoDelayedBranches() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger firstDelay = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger firstDelayBranch = TreeLoggers.delayedBranch(firstDelay, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		firstDelayBranch.log(TreeLogger.DEBUG, "3", null);
		assertEquals("" + messages, 3, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..2", messages.get(1));
		assertEquals("" + messages, "...3", messages.get(2));

		final TreeLogger secondDelay = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "4", null, null);
		assertEquals("" + messages, 3, messages.size());

		final TreeLogger secondDelayBranch = TreeLoggers.delayedBranch(secondDelay, TreeLogger.DEBUG, "5", null, null);
		assertEquals("" + messages, 3, messages.size());

		secondDelayBranch.log(TreeLogger.DEBUG, "6", null);
		assertEquals("" + messages, 6, messages.size());
		assertEquals("" + messages, ".4", messages.get(3));
		assertEquals("" + messages, "..5", messages.get(4));
		assertEquals("" + messages, "...6", messages.get(5));
	}

	public void testTwoDelayedBranchesEachWithLostFirstBranches() {
		final List<String> messages = new ArrayList<String>();
		final MessageCapturer capturer = new MessageCapturer(messages);

		final TreeLogger firstDelay = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "1", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger lostFirstDelayBranch = TreeLoggers.delayedBranch(firstDelay, TreeLogger.DEBUG, "2", null, null);
		assertEquals("" + messages, 0, messages.size());

		final TreeLogger firstDelayBranch = TreeLoggers.delayedBranch(firstDelay, TreeLogger.DEBUG, "3", null, null);
		assertEquals("" + messages, 0, messages.size());

		firstDelayBranch.log(TreeLogger.DEBUG, "4", null);
		assertEquals("" + messages, 3, messages.size());
		assertEquals("" + messages, ".1", messages.get(0));
		assertEquals("" + messages, "..3", messages.get(1));
		assertEquals("" + messages, "...4", messages.get(2));

		final TreeLogger secondDelay = TreeLoggers.delayedBranch(capturer, TreeLogger.DEBUG, "5", null, null);
		assertEquals("" + messages, 3, messages.size());

		final TreeLogger lostSecondDelayBranch = TreeLoggers.delayedBranch(secondDelay, TreeLogger.DEBUG, "6", null, null);
		assertEquals("" + messages, 3, messages.size());

		final TreeLogger secondDelayBranch = TreeLoggers.delayedBranch(secondDelay, TreeLogger.DEBUG, "7", null, null);
		assertEquals("" + messages, 3, messages.size());

		secondDelayBranch.log(TreeLogger.DEBUG, "8", null);
		assertEquals("" + messages, 6, messages.size());
		assertEquals("" + messages, ".5", messages.get(3));
		assertEquals("" + messages, "..7", messages.get(4));
		assertEquals("" + messages, "...8", messages.get(5));
	}

	static class MessageCapturer extends TreeLogger {

		MessageCapturer(final List<String> messages) {
			this(".", messages);
		}

		private MessageCapturer(final String prefix, final List<String> messages) {
			super();
			this.setMessages(messages);
			this.setPrefix(prefix);
		}

		public TreeLogger branch(final TreeLogger.Type type, final String message, final Throwable caught, final HelpInfo helpInfo) {
			final List<String> messages = this.getMessages();
			messages.add(this.getPrefix() + message);
			return new MessageCapturer(this.getPrefix() + ".", messages);
		}

		public boolean isLoggable(final TreeLogger.Type type) {
			return true;
		}

		public void log(final TreeLogger.Type type, final String message, final Throwable caught, final HelpInfo helpInfo) {
			this.getMessages().add(this.getPrefix() + message);
		}

		List<String> messages;

		List<String> getMessages() {
			return this.messages;
		}

		void setMessages(final List<String> messages) {
			this.messages = messages;
		}

		String prefix;

		String getPrefix() {
			return this.prefix;
		}

		void setPrefix(final String prefix) {
			this.prefix = prefix;
		}
	}
}
