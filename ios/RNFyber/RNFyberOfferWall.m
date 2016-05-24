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

RCT_EXPORT_METHOD(showOfferWall:(NSString *)userId)
{
  FYBSDKOptions *options = [FYBSDKOptions optionsWithAppId:@"44761"
                                                    userId:userId
                                             securityToken:@"bf2b549aa134a0a18171ca1219d3d92d"];
  [FyberSDK startWithOptions:options];
  FYBOfferWallViewController *offerWallViewController = [FyberSDK offerWallViewController];
  //
  // Show the Offer Wall
  //
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
