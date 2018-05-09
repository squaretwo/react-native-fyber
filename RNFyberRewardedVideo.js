import { NativeModules, NativeEventEmitter } from 'react-native';

const RNFyberRewardedVideo = NativeModules.RNFyberRewardedVideo;
const RNFyberRewardedVideoEventEmitter = new NativeEventEmitter(RNFyberRewardedVideo);

const eventHandlers = {
  rewardedVideoReceived: new Map(),
  rewardedVideoFailedToLoad: new Map(),
  rewardedVideoDidStart: new Map(),
  rewardedVideoClosedByUser: new Map(),
  rewardedVideoClosedByError: new Map()
};

const addEventListener = (type, handler) => {
  switch (type) {
    case 'rewardedVideoReceived':
      eventHandlers[type].set(handler, RNFyberRewardedVideoEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoFailedToLoad':
      eventHandlers[type].set(handler, RNFyberRewardedVideoEventEmitter.addListener(type, (error) => { handler(error); }));
      break;
    case 'rewardedVideoDidStart':
      eventHandlers[type].set(handler, RNFyberRewardedVideoEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoClosedByUser':
      eventHandlers[type].set(handler, RNFyberRewardedVideoEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoClosedByError':
      eventHandlers[type].set(handler, RNFyberRewardedVideoEventEmitter.addListener(type, handler));
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
  RNFyberRewardedVideoEventEmitter.removeAllListeners('rewardedVideoReceived');
  RNFyberRewardedVideoEventEmitter.removeAllListeners('rewardedVideoFailedToLoad');
  RNFyberRewardedVideoEventEmitter.removeAllListeners('rewardedVideoDidStart');
  RNFyberRewardedVideoEventEmitter.removeAllListeners('rewardedVideoClosedByUser');
  RNFyberRewardedVideoEventEmitter.removeAllListeners('rewardedVideoClosedByError');
};

module.exports = {
  ...RNFyberRewardedVideo,
  requestRewardedVideo: (cb = () => {}) => RNFyberRewardedVideo.requestRewardedVideo(cb),
  showRewardedVideo: () => RNFyberRewardedVideo.showRewardedVideo(),
  addEventListener,
  removeEventListener,
  removeAllListeners
};
