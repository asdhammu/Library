import {Author} from '../Model/Author'
import {Borrower} from "./Borrower";
/**
 * Created by asdha on 3/14/2017.
 */

export class SearchResult{

  public isbn:string;
  public title:string;
  public cover:string;
  public pages:string;
  public available:boolean;
  public author:Author[];
  public borrower:Borrower;


}
