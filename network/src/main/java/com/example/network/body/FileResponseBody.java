package com.example.network.body;

import android.util.Log;

import com.example.network.event.FileLoadEvent;
import com.example.network.inter.IDownFileProgress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

public class FileResponseBody extends ResponseBody {
    private Response mResponse;
    FileLoadEvent mFileLoadEvent = new FileLoadEvent();
    private IDownFileProgress mProgress;


    public FileResponseBody(Response originResponse, IDownFileProgress progress) {
        mResponse = originResponse;
        mProgress = progress;
    }

    @Override
    public MediaType contentType() {
        return mResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        long contentLength = mResponse.body().contentLength();
        mFileLoadEvent.setTotalCount(contentLength);
        return contentLength;
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(mResponse.body().source()) {
            long mByteRead = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                mByteRead += byteRead == -1 ? 0 : byteRead;
                Log.e("tag", "read: ----byteRead=" + mByteRead + ",sink=" + sink + ",byteCount=" + byteCount);
                mFileLoadEvent.setCurrentCount(mByteRead);
                Log.e("tag", "read: -----contentLength=" + mFileLoadEvent.getTotalCount() + ",CurrentCount=" + mFileLoadEvent.getCurrentCount());
                if (mProgress != null) {
                    mProgress.progress(mFileLoadEvent);
                }
                return mByteRead;
            }
        });
    }
}
