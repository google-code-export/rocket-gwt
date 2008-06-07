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
package rocket.compiler;

import java.util.Collection;

import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.HelpInfo;
import com.google.gwt.core.ext.TreeLogger.Type;

/**
 * A collection of useful TreeLogger factory methods.
 * 
 * @author Miroslav Pokorny
 */
public class TreeLoggers {

	/**
	 * Creates a branch from the given logger but doesnt actually logs the given
	 * message. The branch is delayed until an actual log. Any new branch also
	 * doesnt commit any messages until the new branch has a message logged. If
	 * no messages are logged the original message is also lost.
	 * 
	 * @param logger
	 * @param type
	 * @param msg
	 * @param caught
	 * @param helpInfo
	 * @return
	 */
	static public TreeLogger delayedBranch(final TreeLogger logger, final Type type, final String message, final Throwable caught,
			final HelpInfo helpInfo) {
		return new DelayedBranchTreeLogger(logger, type, message, caught, helpInfo);
	}

	static class DelayedBranchTreeLogger extends TreeLogger {

		DelayedBranchTreeLogger(final TreeLogger logger, final Type type, final String message, final Throwable caught,
				final HelpInfo helpInfo) {
			this.setCaught(caught);
			this.setMessage(message);
			this.setLogger(logger);
			this.setType(type);
			this.setHelpInfo(helpInfo);
		}

		public boolean isLoggable(final Type type0) {
			return this.getLogger().isLoggable(type0);
		}

		public TreeLogger branch(final Type type0, final String message0, final Throwable caught0, final HelpInfo helpInfo0) {
			return TreeLoggers.delayedBranch(this, type0, message0, caught0, helpInfo0);
		}

		public void log(final Type type0, final String message0, final Throwable caught0, final TreeLogger.HelpInfo helpInfo0) {
			TreeLogger branch = this.getBranch();
			if (null == branch) {
				final Type type = this.getType();
				final String message = this.getMessage();
				final Throwable caught = this.getCaught();
				final HelpInfo helpInfo = this.getHelpInfo();
				branch = commitBranch(type, message, caught, helpInfo);
				this.setBranch(branch);
			}
			branch.log(type0, message0, caught0);
		}

		public TreeLogger commitBranch(final Type type, final String message, final Throwable caught, final HelpInfo helpInfo) {
			TreeLogger logger = this.getLogger();

			if (logger instanceof DelayedBranchTreeLogger) {
				final DelayedBranchTreeLogger delayed = (DelayedBranchTreeLogger) logger;

				logger = delayed.getBranch();
				if (null == logger) {
					logger = delayed.commitBranch(type, message, caught, helpInfo);
					delayed.setBranch(logger);
				}
			}

			if (logger instanceof DelayedBranchTreeLogger) {
				throw new AssertionError("logger shouldnt be a DelayedBranchTreeLogger");
			}

			final Type type0 = this.getType();
			final String message0 = this.getMessage();
			final Throwable caught0 = this.getCaught();
			final HelpInfo helpInfo0 = this.getHelpInfo();
			logger = logger.branch(type0, message0, caught0, helpInfo0);

			return logger;
		}

		TreeLogger branch;

		TreeLogger getBranch() {
			return this.branch;
		}

		void setBranch(final TreeLogger branch) {
			this.branch = branch;
		}

		private TreeLogger logger;

		public TreeLogger getLogger() {
			return this.logger;
		}

		void setLogger(final TreeLogger logger) {
			this.logger = logger;
		}

		private Type type;

		public Type getType() {
			return this.type;
		}

		void setType(final Type type) {
			this.type = type;
		}

		private String message;

		public String getMessage() {
			return message;
		}

		void setMessage(final String message) {
			this.message = message;
		}

		private Throwable caught;

		public Throwable getCaught() {
			return this.caught;
		}

		void setCaught(final Throwable caught) {
			this.caught = caught;
		}

		private HelpInfo helpInfo;

		public HelpInfo getHelpInfo() {
			return this.helpInfo;
		}

		void setHelpInfo(final HelpInfo helpInfo) {
			this.helpInfo = helpInfo;
		}
	}

	/**
	 * Creates a TreeLogger that pipes any messages to the given logger as well
	 * as recording each message to the given collection. Subsequent branches
	 * will also have their messages captured.
	 * 
	 * @param logger
	 * @param messages
	 * @return
	 */
	static public TreeLogger pipeAndCapture(final TreeLogger logger, final Collection messages) {
		Checker.notNull("parameter:logger", logger);
		Checker.notNull("parameter:messages", messages);

		return new TreeLogger() {

			public TreeLogger branch(final TreeLogger.Type type, final String message, final Throwable caught, final HelpInfo helpInfo) {
				if (this.isLoggable(type)) {
					messages.add(message);
				}
				final TreeLogger branch = TreeLoggers.pipeAndCapture(logger.branch(type, message, caught), messages);
				return branch;
			}

			public boolean isLoggable(final TreeLogger.Type type) {
				return true;
			}

			public void log(final TreeLogger.Type type, final String message, final Throwable caught, final HelpInfo helpInfo) {
				if (this.isLoggable(type)) {
					messages.add(message);
				}
				logger.log(type, message, caught);
			}
		};
	}
}
