/*
 * Copyright (C) 2016  Luke Melaia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lmelaia.iseries.common.system;

/**
 * All available exit codes for the application.
 * <p>
 * An exit code contains:
 * the integer exit code for use in {@link System#exit(int)}.
 * the reason for the exit.
 * if the exit was caused by an error (signals the application to crash if true).
 */
public enum ExitCode {

    /**
     * Normal termination.
     */
    NORMAL(0, "Normal exit.", false),

    /**
     * Termination caused by the user forcibly terminating
     * the application through the os (e.g. through task
     * managers 'end task' on windows), a crash in the jvm
     * or an instance not being able to start (eg. jar not found).
     */
    FORCED_EXIT(1, "An instance was forcibly closed, crashed or could not be started.", true),

    /**
     * Exit code used for testing.
     */
    TEST_EXIT(10, "Test exit.", true),

    /**
     * An exception went uncaught and propagated to
     * the uncaught exception handler.
     */
    UNHANDLED_EXCEPTION(11, "An unhandled error occurred preventing the application from continuing.", true),

    /**
     * The launcher is not responding to messages and/or pings.
     */
    UNRESPONSIVE_LAUNCHER(12, "The launcher has closed or become unresponsive.", true),

    /**
     * Failed to xcom with the launcher due to an exception.
     */
    LAUNCHER_COM_FAILED(13, "Failed to communicate with the launcher.", true);

    /**
     * The exit code for use in {@link System#exit(int)}.
     */
    public final int code;

    /**
     * Reason as to why the exit happened.
     */
    public final String description;

    /**
     * Is the exit due to an error.
     */
    public final boolean error;

    /**
     * Is the exit requested by the user
     * or by the application completing
     * successfully.
     */
    public final boolean normal;

    /**
     * The the request come from the
     * launcher application or I-Series
     * application.
     */
    private boolean fromLauncher = true;

    /**
     * Constructor.
     *
     * @param code        The exit code for use in {@link System#exit(int)}.
     * @param description Reason as to why the exit happened.
     * @param error       Is the exit due to an error.
     */
    ExitCode(int code, String description, boolean error) {
        this.code = code;
        this.description = description;
        this.error = error;
        this.normal = code == 0;
    }

    /**
     * @param code the given exit code.
     * @return the ExitCode with the given code.
     */
    public static ExitCode getFromCode(int code) {
        for (ExitCode ec : values())
            if (ec.code == code)
                return ec;

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getClass().toString()
                + String.format("[code=%s, name=%s, description=%s, error=%s]",
                code, this.name(), description, error);
    }

    /**
     * @return {@code true} if the exit was requested
     * from the launcher jar.
     */
    public boolean isFromLauncher() {
        return fromLauncher;
    }

    /**
     * Flags the exit request as originating
     * from the launcher jar.
     *
     * @param fromLauncher true if requested by the launcher jar.
     * @return the exit request.
     */
    public ExitCode setFromLauncher(boolean fromLauncher) {
        this.fromLauncher = fromLauncher;
        return this;
    }
}
