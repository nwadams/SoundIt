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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    //grab our deviceID
    NSString *thisDeviceUniqueIDentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    NSLog(@"thisDeviceUniqueIDentifier reads: %@", thisDeviceUniqueIDentifier);
    
    //send DeviceID and send to backend before continuing
    NSLog(@"Shooting deviceID to Anuj right now of string %@", thisDeviceUniqueIDentifier);
    NSMutableDictionary *params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                   thisDeviceUniqueIDentifier, @"device_id",
                                   @"pass", @"password",
                                   nil];
    
    //DEBUG
    NSLog(@"[params objectForKey:xxx] is returning %@", [params objectForKey:@"command"]);
        
    [[API sharedInstance] callAPIMethod:@"signUp"
                             withParams:params
                           onCompletion:^(NSMutableString *responseString){
                               NSLog(@"responseString reads %@", responseString);
                               [responseString replaceOccurrencesOfString:@"\\" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, responseString.length)];
                               NSLog(@"...responseString after / strip now reads %@", responseString);
                               [responseString replaceOccurrencesOfString:@"\"[" withString:@"[" options:NSCaseInsensitiveSearch range:NSMakeRange(0, responseString.length)];
                               NSLog(@"...responseString after \"[ strip now reads %@", responseString);
                               [responseString replaceOccurrencesOfString:@"]\"" withString:@"]" options:NSCaseInsensitiveSearch range:NSMakeRange(0, responseString.length)];
                               NSLog(@"...and finally responseString is now %@", responseString);
                               
                               SBJsonParser *parser = [[SBJsonParser alloc] init];
                               NSArray *json = [parser objectWithString:responseString];
                               NSArray *user = [[json objectAtIndex:0] objectForKey:@"user"];
                               NSDictionary *idDict = [user objectAtIndex:0];
                               NSLog(@"idDict value reads: %@", [idDict objectForKey:@"id"]);
                               
                               NSLog(@"json description: %@", [json description]);
                               
                               
//                               NSError *error;
//                               NSData *jsonData = [responseString dataUsingEncoding:NSUTF8StringEncoding];
//                               NSArray *jsonRep = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&error];
//                               
//                               NSLog(@"%i", jsonRep.count);
                               
//                               NSLog(@"jsonDict description reads %@", [[jsonArray objectAtIndex:0] description]);
                               
//                               NSString *jsonString = [[NSString alloc] initWithData:responseString encoding:NSUTF8StringEncoding];
//                               NSDictionary *results = [jsonString JSONvalue];
//                              
//                               SBJsonParser *parser = [[SBJsonParser alloc] init];
//                               id jsonRepresentation = [parser objectWithString:responseString];
//                               
//                               NSLog(@"jsonRepresentation is: %@", [jsonRepresentation description]);
                               
//                               [NSMutableString string]
//                               
//                               NSLog(@"string is: %@", [[json objectForKey:0] objectForKey:@"user"]);
//                               
//                               //check if json error
//                               if([json objectForKey:@"error"] == nil){
//                                   //chuck error & show error
//                                   [UIAlertView error:[json objectForKey:@"error"]];
//                                   
//                               } else {//no json error so...
//                                   //segue to PlaylistTVC
//                                   [self performSegueWithIdentifier:@"SplashToPlaylistTVC" sender:nil];
//                                   
//                               }
                               
                           }];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
