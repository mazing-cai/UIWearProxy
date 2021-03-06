package edu.stonybrook.cs.netsys.uiwearproxy.uiwearService;

import java.util.HashSet;

import edu.stonybrook.cs.netsys.uiwearlib.dataProtocol.AccNode;

/**
 * Created by qqcao on 11/5/16.
 *
 * For app preference nodes ready callback
 */
interface AppNodesReadyCallback {
    void onAppNodesReady(String cacheKey, HashSet<AccNode> nodes);
}
