//
//  RNFyberRewardedVideo.h
//  RNFyberRewardedVideo
//
//  Created by Ben Yee <benyee@gmail.com> on 11/14/16.
//
#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#else
#import "RCTBridgeModule.h"
#endif

#if __has_include(<React/RCTEventEmitter.h>)
#import <React/RCTEventEmitter.h>
#else
#import "RCTEventEmitter.h"
#endif

#import "FyberSDK.h"

@interface RNFyberRewardedVideo : RCTEventEmitter <RCTBridgeModule, FYBRewardedVideoControllerDelegate>
@end
