'use strict';

import {
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

const RNFyberRewardedVideo = NativeModules.RNFyberRewardedVideo;

const eventHandlers = {
  rewardedVideoReceived: new Map(),
  rewardedVideoFailedToLoad: new Map(),
  rewardedVideoDidStart: new Map()
};

const addEventListener = (type, handler) => {
  switch (type) {
    case 'rewardedVideoReceived':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoFailedToLoad':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, (error) => { handler(error); }));
      break;
    case 'rewardedVideoDidStart':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    default:
      console.log(`Event with type ${type} does not exist.`);
    }
}

const removeEventListener = (type, handler) => {
  if (!eventHandlers[type].has(handler)) {
    return;
  }
  eventHandlers[type].get(handler).remove();
  eventHandlers[type].delete(handler);
}

const removeAllListeners = () => {
  DeviceEventEmitter.removeAllListeners('rewardedVideoReceived');
  DeviceEventEmitter.removeAllListeners('rewardedVideoFailedToLoad');
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidStart');
};

module.exports = {
  ...RNFyberRewardedVideo,
  requestRewardedVideo: (cb = () => {}) => RNFyberRewardedVideo.requestRewardedVideo(cb),
  showRewardedVideo: () => RNFyberRewardedVideo.showRewardedVideo(),
  addEventListener,
  removeEventListener,
  removeAllListeners
};
