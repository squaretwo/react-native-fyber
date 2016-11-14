//
//  RNFyberRewardedVideo.h
//  RNFyberRewardedVideo
//
//  Created by Ben Yee <benyee@gmail.com> on 11/14/16.
//
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#import "FyberSDK.h"

@interface RNFyberRewardedVideo : NSObject <RCTBridgeModule, FYBRewardedVideoControllerDelegate>
@end
