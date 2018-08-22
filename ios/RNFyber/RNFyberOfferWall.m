//
//  RNFyberOfferWall.m
//  RNFyberOfferWall
//
//  Created by Ben Yee <benyee@gmail.com> on 5/20/16.
//
#import "RNFyberOfferWall.h"

@implementation RNFyberOfferWall {
}

// Run on the main thread
- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

// Initialize Fyber before showing the offer wall
RCT_EXPORT_METHOD(initializeOfferWall:(NSString *)appId securityToken:(NSString *)securityToken userId:(NSString *)userId callBack:(RCTResponseSenderBlock)errorCallback)
{
  FYBSDKOptions *options = [FYBSDKOptions optionsWithAppId:appId
                                                    userId:userId
                                             securityToken:securityToken];
  [FyberSDK startWithOptions:options];
}

//
// Show the Offer Wall
//
RCT_EXPORT_METHOD(showOfferWall)
{

  FYBOfferWallViewController *offerWallViewController = [FyberSDK offerWallViewController];
  [offerWallViewController presentFromViewController:[UIApplication sharedApplication].delegate.window.rootViewController animated:YES completion:^{

    NSLog(@"Offer Wall presented");

  } dismiss:^(NSError *error) {

    //
    // Code executed when the Offer Wall is dismissed
    // If an error occurred, the error parameter describes the error otherwise this value is nil
    //
    if (error) {
      NSLog(@"Offer Wall error");
    }

  }];
}

@end
