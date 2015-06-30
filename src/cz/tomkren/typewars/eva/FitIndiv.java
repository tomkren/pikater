package cz.tomkren.typewars.eva;

public interface FitIndiv extends Probable {
    FitVal evaluate(FitFun fitness);
}
