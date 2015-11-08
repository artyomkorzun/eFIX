/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.f1x.message;

public class AdminMessageTypes {
    public static final byte LOGON = 'A';
    public static final byte HEARTBEAT = '0';
    public static final byte TEST = '1';
    public static final byte RESEND = '2';
    public static final byte REJECT = '3';
    public static final byte RESET = '4';
    public static final byte LOGOUT = '5';

    public static boolean isAdmin(CharSequence msgType) {
        if (msgType.length() != 1)
            return false;

        char charMsgType = msgType.charAt(0);
        return HEARTBEAT <= charMsgType && charMsgType <= LOGOUT ||
                charMsgType == LOGON;
    }
}
