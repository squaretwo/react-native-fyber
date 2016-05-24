//
//  RNFyber.m
//  RNFyber
//
//  Created by Ben Yee <benyee@gmail.com> on 5/20/16.
//
#import "RNFyber.h"

@implementation RNFyber

RCT_EXPORT_MODULE();

// Available as NativeModules.RNFyber.processString
RCT_EXPORT_METHOD(processString:(NSString *)input callback:(RCTResponseSenderBlock)callback)
{
    callback(@[[input stringByReplacingOccurrencesOfString:@"Goodbye" withString:@"verydice"]]);
}

@end
