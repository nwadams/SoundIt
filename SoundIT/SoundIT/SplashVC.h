//
//  SplashVC.h
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-25.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "API.h"
#import "UIAlertView+error.h"
#import "SBJson.h"

@interface SplashVC : UIViewController

-(void)attemptConnectionWithBackend;

@end
