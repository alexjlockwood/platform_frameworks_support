/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.media2;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.ComponentName;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.util.ObjectsCompat;
import androidx.media2.SessionToken2.SessionToken2Impl;
import androidx.media2.SessionToken2.TokenType;
import androidx.versionedparcelable.ParcelField;
import androidx.versionedparcelable.VersionedParcelize;

@VersionedParcelize
final class SessionToken2ImplBase implements SessionToken2Impl {
    @ParcelField(1)
    int mUid;
    @ParcelField(2)
    @TokenType int mType;
    @ParcelField(3)
    String mPackageName;
    @ParcelField(4)
    String mServiceName;
    @ParcelField(5)
    IBinder mISession2;
    @ParcelField(6)
    ComponentName mComponentName;

    /**
     * Constructor for the token. You can only create token for session service or library service
     * to use by {@link MediaController2} or {@link MediaBrowser2}.
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    SessionToken2ImplBase(@NonNull ComponentName serviceComponent, int uid, int type) {
        if (serviceComponent == null) {
            throw new IllegalArgumentException("serviceComponent shouldn't be null");
        }
        mComponentName = serviceComponent;
        mPackageName = serviceComponent.getPackageName();
        mServiceName = serviceComponent.getClassName();
        mUid = uid;
        mType = type;
        mISession2 = null;
    }

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    SessionToken2ImplBase(int uid, int type, String packageName, IMediaSession2 iSession2) {
        mUid = uid;
        mType = type;
        mPackageName = packageName;
        mServiceName = null;
        mComponentName = null;
        mISession2 = iSession2.asBinder();
    }

    /**
     * Used for {@link VersionedParcelize}.
     * @hide
     */
    @RestrictTo(LIBRARY)
    SessionToken2ImplBase() {
        // Do nothing.
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(mType, mUid, mPackageName, mServiceName);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SessionToken2ImplBase)) {
            return false;
        }
        SessionToken2ImplBase other = (SessionToken2ImplBase) obj;
        return mUid == other.mUid
                && TextUtils.equals(mPackageName, other.mPackageName)
                && TextUtils.equals(mServiceName, other.mServiceName)
                && mType == other.mType
                && ObjectsCompat.equals(mISession2, other.mISession2);
    }

    @Override
    public String toString() {
        return "SessionToken {pkg=" + mPackageName + " type=" + mType
                + " service=" + mServiceName + " IMediaSession2=" + mISession2 + "}";
    }

    @Override
    public boolean isLegacySession() {
        return false;
    }

    @Override
    public int getUid() {
        return mUid;
    }

    @Override
    public @NonNull String getPackageName() {
        return mPackageName;
    }

    @Override
    public @Nullable String getServiceName() {
        return mServiceName;
    }

    /**
     * @hide
     * @return component name of this session token. Can be null for TYPE_SESSION.
     */
    @RestrictTo(LIBRARY_GROUP)
    @Override
    public ComponentName getComponentName() {
        return mComponentName;
    }

    @Override
    public @TokenType int getType() {
        return mType;
    }

    @Override
    public Object getBinder() {
        return mISession2;
    }
}
