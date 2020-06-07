import { Author } from "./author";
import { Borrower } from './borrower';
/**
 * Created by asdha on 3/14/2017.
 */
export class Book {

  public isbn: string;
  public title: string;
  public cover: string;
  public pages: string;
  public available: boolean;
  public author: Author[];
  borrower: Borrower;


}


