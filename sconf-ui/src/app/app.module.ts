import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import { DragDropModule } from '@angular/cdk/drag-drop';
import zh from '@angular/common/locales/zh';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ClientComponent } from './client/client.component';
import { NZ_I18N, zh_CN } from 'ng-zorro-antd/i18n';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { NgProgressModule } from 'ngx-progressbar';
import { NzResizableModule } from 'ng-zorro-antd/resizable';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzCascaderModule } from 'ng-zorro-antd/cascader';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { NzCodeEditorModule } from 'ng-zorro-antd/code-editor';
import {NzSwitchModule} from 'ng-zorro-antd/switch';

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ClientComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        DragDropModule,
        NzIconModule,
        NzLayoutModule,
        NzInputModule,
        NzButtonModule,
        NzTabsModule,
        NzTableModule,
        NzGridModule,
        NzModalModule,
        NzFormModule,
        AppRoutingModule,
        NzMessageModule,
        NzPopconfirmModule,
        NgProgressModule,
        NzResizableModule,
        NzMenuModule,
        NzToolTipModule,
        NzDropDownModule,
        NzCascaderModule,
        NzRadioModule,
        NzCodeEditorModule,
        NzSwitchModule,
    ],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [AppComponent]
})
export class AppModule { }
