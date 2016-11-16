//
//  RNFyberRewardedVideo.m
//  RNFyberRewardedVideo
//
//  Created by Ben Yee <benyee@gmail.com> on 11/14/16.
//
#import "RNFyberRewardedVideo.h"

@implementation RNFyberRewardedVideo {
    FYBRewardedVideoController *rewardedVideoController;
    RCTResponseSenderBlock _requestVideoCallback;
}

@synthesize bridge = _bridge;


// Run on the main thread
- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

#pragma mark exported methods

// Request the video before we need to display it
RCT_EXPORT_METHOD(requestRewardedVideo:(RCTResponseSenderBlock)callback)
{
    NSLog(@"requestRewardedVideo!");
    rewardedVideoController = [FyberSDK rewardedVideoController];
    rewardedVideoController.delegate = self;
    [rewardedVideoController requestVideo];
    _requestVideoCallback = callback;
}

//
// Show the RewardedVideo
//
RCT_EXPORT_METHOD(showRewardedVideo)
{
    NSLog(@"showRewardedVideo!");
    // Play the received rewarded video
    [rewardedVideoController presentRewardedVideoFromViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
}


#pragma mark delegate events

- (void)rewardedVideoControllerDidReceiveVideo:(FYBRewardedVideoController *)rewardedVideoController {
    NSLog(@">>>>>>>>>>>> rewardedVideoControllerDidReceiveVideo!");
   [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoReceived" body:nil];
    _requestVideoCallback(@[[NSNull null]]);
}

-(void)rewardedVideoController:(FYBRewardedVideoController *)rewardedVideoController didFailToReceiveVideoWithError:(NSError *)error{
    NSLog(@"An error occured while receiving the video ad %@", error);
    [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoFailedToLoad" body:nil];
    _requestVideoCallback(@[[error description]]);
}

-(void)rewardedVideoControllerDidStartVideo:(FYBRewardedVideoController *)rewardedVideoController{
    NSLog(@">>>> AWW YA ---- A video has just been presented");
   [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidStart" body:nil];

}


-(void)rewardedVideoController:(FYBRewardedVideoController *)rewardedVideoController didDismissVideoWithReason:(FYBRewardedVideoControllerDismissReason)reason{
    
    NSString *reasonDescription;
    switch (reason) {
        case FYBRewardedVideoControllerDismissReasonError:
            reasonDescription = @"because of an error during playing";
            [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoClosedByError" body:nil];
            break;
        case FYBRewardedVideoControllerDismissReasonUserEngaged:
            reasonDescription = @"because the user clicked on it";
            [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoClosedByUser" body:nil];
            break;
        case FYBRewardedVideoControllerDismissReasonAborted:
            reasonDescription = @"because the user explicitly closed it";
            break;
    }
    
    NSLog(@"The video ad was dismissed %@", reasonDescription);
}



@end
