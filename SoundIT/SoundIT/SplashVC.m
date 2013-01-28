//
//  SplashVC.m
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-25.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import "SplashVC.h"

@interface SplashVC ()

@end

@implementation SplashVC

-(void)viewDidAppear:(BOOL)animated{
    [self attemptConnectionWithBackend];
}

-(void)attemptConnectionWithBackend{
    //grab our deviceID
    NSString *thisDeviceUniqueIDentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"thisDeviceUniqueIDentifier reads: %@", thisDeviceUniqueIDentifier);
    
    //send DeviceID and send to backend before continuing
    NSLog(@"Shooting deviceID to Anuj right now of string %@", thisDeviceUniqueIDentifier);
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   thisDeviceUniqueIDentifier, @"device_id",
                                   @"pass", @"password",
                                   nil];
    
    [[API sharedInstance] callAPIMethod:@"signUp"
                             withParams:params
                           onCompletion:^(NSArray *json){
                               if([[json objectAtIndex:0] objectForKey:@"Error Message"] != nil){
                                   //handle error
                                   [UIAlertView error:(NSString *)[[json objectAtIndex:0] valueForKey:@"Error Message"]];
                                   
                               } else {
                                   //segue to PlaylistTVC
                                   [self performSegueWithIdentifier:@"SplashVCToPlaylistTVC" sender:nil];
                                   
                                   //could do something fun like "Welcome to thePit (Powered by SoundIT)"
                               }
                               
                           }];
}

@end
