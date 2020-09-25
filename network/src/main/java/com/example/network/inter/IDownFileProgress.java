package com.example.network.inter;

import com.example.network.event.FileLoadEvent;

public interface IDownFileProgress {
    void progress(FileLoadEvent event);
}
