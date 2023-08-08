package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            int i4 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i4;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (constraintWidgetContainer.optimizeFor(4)) {
                if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                    applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
                }
            } else {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0035, code lost:
        if (r2.mHorizontalChainStyle == 2) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0048, code lost:
        if (r2.mVerticalChainStyle == 2) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x004a, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x004c, code lost:
        r5 = false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0488  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x04bd  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x04e7  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x04f2  */
    /* JADX WARN: Removed duplicated region for block: B:279:0x04f6  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0507  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x050a  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0395 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x015f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i, int i2, ChainHead chainHead) {
        boolean z;
        boolean z2;
        boolean z3;
        ArrayList<ConstraintWidget> arrayList;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        ConstraintAnchor constraintAnchor3;
        int i3;
        ConstraintWidget constraintWidget2;
        int i4;
        ConstraintAnchor constraintAnchor4;
        SolverVariable solverVariable;
        SolverVariable solverVariable2;
        ConstraintWidget constraintWidget3;
        ConstraintAnchor constraintAnchor5;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        ConstraintWidget constraintWidget4;
        SolverVariable solverVariable5;
        SolverVariable solverVariable6;
        float f;
        int size;
        int i5;
        ArrayList<ConstraintWidget> arrayList2;
        int i6;
        boolean z4;
        int i7;
        boolean z5;
        ConstraintWidget constraintWidget5;
        boolean z6;
        int i8;
        ConstraintWidget constraintWidget6 = chainHead.mFirst;
        ConstraintWidget constraintWidget7 = chainHead.mLast;
        ConstraintWidget constraintWidget8 = chainHead.mFirstVisibleWidget;
        ConstraintWidget constraintWidget9 = chainHead.mLastVisibleWidget;
        ConstraintWidget constraintWidget10 = chainHead.mHead;
        float f2 = chainHead.mTotalWeight;
        ConstraintWidget constraintWidget11 = chainHead.mFirstMatchConstraintWidget;
        ConstraintWidget constraintWidget12 = chainHead.mLastMatchConstraintWidget;
        boolean z7 = constraintWidgetContainer.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (i == 0) {
            z = constraintWidget10.mHorizontalChainStyle == 0;
            z2 = constraintWidget10.mHorizontalChainStyle == 1;
        } else {
            z = constraintWidget10.mVerticalChainStyle == 0;
            z2 = constraintWidget10.mVerticalChainStyle == 1;
        }
        ConstraintWidget constraintWidget13 = constraintWidget6;
        boolean z8 = z2;
        boolean z9 = z;
        boolean z10 = false;
        while (true) {
            if (z10) {
                break;
            }
            ConstraintAnchor constraintAnchor6 = constraintWidget13.mListAnchors[i2];
            int i9 = (z7 || z3) ? 1 : 4;
            int margin = constraintAnchor6.getMargin();
            float f3 = f2;
            if (constraintAnchor6.mTarget != null && constraintWidget13 != constraintWidget6) {
                margin += constraintAnchor6.mTarget.getMargin();
            }
            int i10 = margin;
            if (z3 && constraintWidget13 != constraintWidget6 && constraintWidget13 != constraintWidget8) {
                z4 = z10;
                z5 = z8;
                i7 = 6;
            } else if (z9 && z7) {
                z4 = z10;
                z5 = z8;
                i7 = 4;
            } else {
                z4 = z10;
                i7 = i9;
                z5 = z8;
            }
            if (constraintAnchor6.mTarget != null) {
                if (constraintWidget13 == constraintWidget8) {
                    z6 = z9;
                    constraintWidget5 = constraintWidget10;
                    linearSystem.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, 5);
                } else {
                    constraintWidget5 = constraintWidget10;
                    z6 = z9;
                    linearSystem.addGreaterThan(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, 6);
                }
                linearSystem.addEquality(constraintAnchor6.mSolverVariable, constraintAnchor6.mTarget.mSolverVariable, i10, i7);
            } else {
                constraintWidget5 = constraintWidget10;
                z6 = z9;
            }
            if (z7) {
                if (constraintWidget13.getVisibility() == 8 || constraintWidget13.mListDimensionBehaviors[i] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    i8 = 0;
                } else {
                    i8 = 0;
                    linearSystem.addGreaterThan(constraintWidget13.mListAnchors[i2 + 1].mSolverVariable, constraintWidget13.mListAnchors[i2].mSolverVariable, 0, 5);
                }
                linearSystem.addGreaterThan(constraintWidget13.mListAnchors[i2].mSolverVariable, constraintWidgetContainer.mListAnchors[i2].mSolverVariable, i8, 6);
            }
            ConstraintAnchor constraintAnchor7 = constraintWidget13.mListAnchors[i2 + 1].mTarget;
            if (constraintAnchor7 != null) {
                ConstraintWidget constraintWidget14 = constraintAnchor7.mOwner;
                if (constraintWidget14.mListAnchors[i2].mTarget != null && constraintWidget14.mListAnchors[i2].mTarget.mOwner == constraintWidget13) {
                    r21 = constraintWidget14;
                }
            }
            if (r21 != null) {
                constraintWidget13 = r21;
                z10 = z4;
            } else {
                z10 = true;
            }
            z8 = z5;
            f2 = f3;
            z9 = z6;
            constraintWidget10 = constraintWidget5;
        }
        ConstraintWidget constraintWidget15 = constraintWidget10;
        float f4 = f2;
        boolean z11 = z9;
        boolean z12 = z8;
        if (constraintWidget9 != null) {
            int i11 = i2 + 1;
            if (constraintWidget7.mListAnchors[i11].mTarget != null) {
                ConstraintAnchor constraintAnchor8 = constraintWidget9.mListAnchors[i11];
                linearSystem.addLowerThan(constraintAnchor8.mSolverVariable, constraintWidget7.mListAnchors[i11].mTarget.mSolverVariable, -constraintAnchor8.getMargin(), 5);
                if (z7) {
                    int i12 = i2 + 1;
                    linearSystem.addGreaterThan(constraintWidgetContainer.mListAnchors[i12].mSolverVariable, constraintWidget7.mListAnchors[i12].mSolverVariable, constraintWidget7.mListAnchors[i12].getMargin(), 6);
                }
                arrayList = chainHead.mWeightedMatchConstraintsWidgets;
                if (arrayList != null && (size = arrayList.size()) > 1) {
                    float f5 = (chainHead.mHasUndefinedWeights || chainHead.mHasComplexMatchWeights) ? f4 : chainHead.mWidgetsMatchCount;
                    float f6 = 0.0f;
                    ConstraintWidget constraintWidget16 = null;
                    i5 = 0;
                    float f7 = 0.0f;
                    while (i5 < size) {
                        ConstraintWidget constraintWidget17 = arrayList.get(i5);
                        float f8 = constraintWidget17.mWeight[i];
                        if (f8 < f6) {
                            if (chainHead.mHasComplexMatchWeights) {
                                linearSystem.addEquality(constraintWidget17.mListAnchors[i2 + 1].mSolverVariable, constraintWidget17.mListAnchors[i2].mSolverVariable, 0, 4);
                                arrayList2 = arrayList;
                                i6 = size;
                                i5++;
                                size = i6;
                                arrayList = arrayList2;
                                f6 = 0.0f;
                            } else {
                                f8 = 1.0f;
                                f6 = 0.0f;
                            }
                        }
                        if (f8 == f6) {
                            linearSystem.addEquality(constraintWidget17.mListAnchors[i2 + 1].mSolverVariable, constraintWidget17.mListAnchors[i2].mSolverVariable, 0, 6);
                            arrayList2 = arrayList;
                            i6 = size;
                            i5++;
                            size = i6;
                            arrayList = arrayList2;
                            f6 = 0.0f;
                        } else {
                            if (constraintWidget16 != null) {
                                SolverVariable solverVariable7 = constraintWidget16.mListAnchors[i2].mSolverVariable;
                                int i13 = i2 + 1;
                                SolverVariable solverVariable8 = constraintWidget16.mListAnchors[i13].mSolverVariable;
                                SolverVariable solverVariable9 = constraintWidget17.mListAnchors[i2].mSolverVariable;
                                arrayList2 = arrayList;
                                SolverVariable solverVariable10 = constraintWidget17.mListAnchors[i13].mSolverVariable;
                                i6 = size;
                                ArrayRow createRow = linearSystem.createRow();
                                createRow.createRowEqualMatchDimensions(f7, f5, f8, solverVariable7, solverVariable8, solverVariable9, solverVariable10);
                                linearSystem.addConstraint(createRow);
                            } else {
                                arrayList2 = arrayList;
                                i6 = size;
                            }
                            f7 = f8;
                            constraintWidget16 = constraintWidget17;
                            i5++;
                            size = i6;
                            arrayList = arrayList2;
                            f6 = 0.0f;
                        }
                    }
                }
                if (constraintWidget8 == null && (constraintWidget8 == constraintWidget9 || z3)) {
                    ConstraintAnchor constraintAnchor9 = constraintWidget6.mListAnchors[i2];
                    int i14 = i2 + 1;
                    ConstraintAnchor constraintAnchor10 = constraintWidget7.mListAnchors[i14];
                    SolverVariable solverVariable11 = constraintWidget6.mListAnchors[i2].mTarget != null ? constraintWidget6.mListAnchors[i2].mTarget.mSolverVariable : null;
                    SolverVariable solverVariable12 = constraintWidget7.mListAnchors[i14].mTarget != null ? constraintWidget7.mListAnchors[i14].mTarget.mSolverVariable : null;
                    if (constraintWidget8 == constraintWidget9) {
                        constraintAnchor9 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor10 = constraintWidget8.mListAnchors[i14];
                    }
                    if (solverVariable11 != null && solverVariable12 != null) {
                        if (i == 0) {
                            f = constraintWidget15.mHorizontalBiasPercent;
                        } else {
                            f = constraintWidget15.mVerticalBiasPercent;
                        }
                        linearSystem.addCentering(constraintAnchor9.mSolverVariable, solverVariable11, constraintAnchor9.getMargin(), f, solverVariable12, constraintAnchor10.mSolverVariable, constraintAnchor10.getMargin(), 5);
                    }
                } else if (z11 || constraintWidget8 == null) {
                    int i15 = 8;
                    if (z12 && constraintWidget8 != null) {
                        boolean z13 = chainHead.mWidgetsMatchCount <= 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
                        constraintWidget = constraintWidget8;
                        ConstraintWidget constraintWidget18 = constraintWidget;
                        while (constraintWidget != null) {
                            ConstraintWidget constraintWidget19 = constraintWidget.mNextChainWidget[i];
                            while (constraintWidget19 != null && constraintWidget19.getVisibility() == i15) {
                                constraintWidget19 = constraintWidget19.mNextChainWidget[i];
                            }
                            if (constraintWidget == constraintWidget8 || constraintWidget == constraintWidget9 || constraintWidget19 == null) {
                                constraintWidget2 = constraintWidget18;
                                i4 = 8;
                            } else {
                                ConstraintWidget constraintWidget20 = constraintWidget19 == constraintWidget9 ? null : constraintWidget19;
                                ConstraintAnchor constraintAnchor11 = constraintWidget.mListAnchors[i2];
                                SolverVariable solverVariable13 = constraintAnchor11.mSolverVariable;
                                if (constraintAnchor11.mTarget != null) {
                                    SolverVariable solverVariable14 = constraintAnchor11.mTarget.mSolverVariable;
                                }
                                int i16 = i2 + 1;
                                SolverVariable solverVariable15 = constraintWidget18.mListAnchors[i16].mSolverVariable;
                                int margin2 = constraintAnchor11.getMargin();
                                int margin3 = constraintWidget.mListAnchors[i16].getMargin();
                                if (constraintWidget20 != null) {
                                    constraintAnchor4 = constraintWidget20.mListAnchors[i2];
                                    solverVariable = constraintAnchor4.mSolverVariable;
                                    solverVariable2 = constraintAnchor4.mTarget != null ? constraintAnchor4.mTarget.mSolverVariable : null;
                                } else {
                                    constraintAnchor4 = constraintWidget.mListAnchors[i16].mTarget;
                                    solverVariable = constraintAnchor4 != null ? constraintAnchor4.mSolverVariable : null;
                                    solverVariable2 = constraintWidget.mListAnchors[i16].mSolverVariable;
                                }
                                if (constraintAnchor4 != null) {
                                    margin3 += constraintAnchor4.getMargin();
                                }
                                int i17 = margin3;
                                if (constraintWidget18 != null) {
                                    margin2 += constraintWidget18.mListAnchors[i16].getMargin();
                                }
                                int i18 = margin2;
                                int i19 = z13 ? 6 : 4;
                                if (solverVariable13 == null || solverVariable15 == null || solverVariable == null || solverVariable2 == null) {
                                    constraintWidget3 = constraintWidget20;
                                    constraintWidget2 = constraintWidget18;
                                    i4 = 8;
                                } else {
                                    constraintWidget3 = constraintWidget20;
                                    constraintWidget2 = constraintWidget18;
                                    i4 = 8;
                                    linearSystem.addCentering(solverVariable13, solverVariable15, i18, 0.5f, solverVariable, solverVariable2, i17, i19);
                                }
                                constraintWidget19 = constraintWidget3;
                            }
                            if (constraintWidget.getVisibility() == i4) {
                                constraintWidget = constraintWidget2;
                            }
                            constraintWidget18 = constraintWidget;
                            i15 = 8;
                            constraintWidget = constraintWidget19;
                        }
                        ConstraintAnchor constraintAnchor12 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor = constraintWidget6.mListAnchors[i2].mTarget;
                        int i20 = i2 + 1;
                        constraintAnchor2 = constraintWidget9.mListAnchors[i20];
                        constraintAnchor3 = constraintWidget7.mListAnchors[i20].mTarget;
                        if (constraintAnchor != null) {
                            i3 = 5;
                        } else if (constraintWidget8 != constraintWidget9) {
                            i3 = 5;
                            linearSystem.addEquality(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 5);
                        } else {
                            i3 = 5;
                            if (constraintAnchor3 != null) {
                                linearSystem.addCentering(constraintAnchor12.mSolverVariable, constraintAnchor.mSolverVariable, constraintAnchor12.getMargin(), 0.5f, constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, constraintAnchor2.getMargin(), 5);
                            }
                        }
                        if (constraintAnchor3 != null && constraintWidget8 != constraintWidget9) {
                            linearSystem.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
                        }
                    }
                } else {
                    boolean z14 = chainHead.mWidgetsMatchCount > 0 && chainHead.mWidgetsCount == chainHead.mWidgetsMatchCount;
                    ConstraintWidget constraintWidget21 = constraintWidget8;
                    ConstraintWidget constraintWidget22 = constraintWidget21;
                    while (constraintWidget21 != null) {
                        ConstraintWidget constraintWidget23 = constraintWidget21.mNextChainWidget[i];
                        while (constraintWidget23 != null && constraintWidget23.getVisibility() == 8) {
                            constraintWidget23 = constraintWidget23.mNextChainWidget[i];
                        }
                        if (constraintWidget23 != null || constraintWidget21 == constraintWidget9) {
                            ConstraintAnchor constraintAnchor13 = constraintWidget21.mListAnchors[i2];
                            SolverVariable solverVariable16 = constraintAnchor13.mSolverVariable;
                            SolverVariable solverVariable17 = constraintAnchor13.mTarget != null ? constraintAnchor13.mTarget.mSolverVariable : null;
                            if (constraintWidget22 != constraintWidget21) {
                                solverVariable17 = constraintWidget22.mListAnchors[i2 + 1].mSolverVariable;
                            } else if (constraintWidget21 == constraintWidget8 && constraintWidget22 == constraintWidget21) {
                                solverVariable17 = constraintWidget6.mListAnchors[i2].mTarget != null ? constraintWidget6.mListAnchors[i2].mTarget.mSolverVariable : null;
                            }
                            int margin4 = constraintAnchor13.getMargin();
                            int i21 = i2 + 1;
                            int margin5 = constraintWidget21.mListAnchors[i21].getMargin();
                            if (constraintWidget23 != null) {
                                constraintAnchor5 = constraintWidget23.mListAnchors[i2];
                                SolverVariable solverVariable18 = constraintAnchor5.mSolverVariable;
                                solverVariable4 = constraintWidget21.mListAnchors[i21].mSolverVariable;
                                solverVariable3 = solverVariable18;
                            } else {
                                constraintAnchor5 = constraintWidget7.mListAnchors[i21].mTarget;
                                solverVariable3 = constraintAnchor5 != null ? constraintAnchor5.mSolverVariable : null;
                                solverVariable4 = constraintWidget21.mListAnchors[i21].mSolverVariable;
                            }
                            if (constraintAnchor5 != null) {
                                margin5 += constraintAnchor5.getMargin();
                            }
                            if (constraintWidget22 != null) {
                                margin4 += constraintWidget22.mListAnchors[i21].getMargin();
                            }
                            if (solverVariable16 != null && solverVariable17 != null && solverVariable3 != null && solverVariable4 != null) {
                                if (constraintWidget21 == constraintWidget8) {
                                    margin4 = constraintWidget8.mListAnchors[i2].getMargin();
                                }
                                int i22 = margin4;
                                constraintWidget4 = constraintWidget23;
                                linearSystem.addCentering(solverVariable16, solverVariable17, i22, 0.5f, solverVariable3, solverVariable4, constraintWidget21 == constraintWidget9 ? constraintWidget9.mListAnchors[i21].getMargin() : margin5, z14 ? 6 : 4);
                                if (constraintWidget21.getVisibility() == 8) {
                                    constraintWidget22 = constraintWidget21;
                                }
                                constraintWidget21 = constraintWidget4;
                            }
                        }
                        constraintWidget4 = constraintWidget23;
                        if (constraintWidget21.getVisibility() == 8) {
                        }
                        constraintWidget21 = constraintWidget4;
                    }
                }
                if ((!z11 || z12) && constraintWidget8 != null) {
                    ConstraintAnchor constraintAnchor14 = constraintWidget8.mListAnchors[i2];
                    int i23 = i2 + 1;
                    ConstraintAnchor constraintAnchor15 = constraintWidget9.mListAnchors[i23];
                    solverVariable5 = constraintAnchor14.mTarget == null ? constraintAnchor14.mTarget.mSolverVariable : null;
                    SolverVariable solverVariable19 = constraintAnchor15.mTarget == null ? constraintAnchor15.mTarget.mSolverVariable : null;
                    if (constraintWidget7 == constraintWidget9) {
                        ConstraintAnchor constraintAnchor16 = constraintWidget7.mListAnchors[i23];
                        solverVariable6 = constraintAnchor16.mTarget != null ? constraintAnchor16.mTarget.mSolverVariable : null;
                    } else {
                        solverVariable6 = solverVariable19;
                    }
                    if (constraintWidget8 == constraintWidget9) {
                        constraintAnchor14 = constraintWidget8.mListAnchors[i2];
                        constraintAnchor15 = constraintWidget8.mListAnchors[i23];
                    }
                    if (solverVariable5 != null || solverVariable6 == null) {
                    }
                    int margin6 = constraintAnchor14.getMargin();
                    if (constraintWidget9 != null) {
                        constraintWidget7 = constraintWidget9;
                    }
                    linearSystem.addCentering(constraintAnchor14.mSolverVariable, solverVariable5, margin6, 0.5f, solverVariable6, constraintAnchor15.mSolverVariable, constraintWidget7.mListAnchors[i23].getMargin(), 5);
                    return;
                }
                return;
            }
        }
        if (z7) {
        }
        arrayList = chainHead.mWeightedMatchConstraintsWidgets;
        if (arrayList != null) {
            if (chainHead.mHasUndefinedWeights) {
            }
            float f62 = 0.0f;
            ConstraintWidget constraintWidget162 = null;
            i5 = 0;
            float f72 = 0.0f;
            while (i5 < size) {
            }
        }
        if (constraintWidget8 == null) {
        }
        if (z11) {
        }
        int i152 = 8;
        if (z12) {
            if (chainHead.mWidgetsMatchCount <= 0) {
            }
            constraintWidget = constraintWidget8;
            ConstraintWidget constraintWidget182 = constraintWidget;
            while (constraintWidget != null) {
            }
            ConstraintAnchor constraintAnchor122 = constraintWidget8.mListAnchors[i2];
            constraintAnchor = constraintWidget6.mListAnchors[i2].mTarget;
            int i202 = i2 + 1;
            constraintAnchor2 = constraintWidget9.mListAnchors[i202];
            constraintAnchor3 = constraintWidget7.mListAnchors[i202].mTarget;
            if (constraintAnchor != null) {
            }
            if (constraintAnchor3 != null) {
                linearSystem.addEquality(constraintAnchor2.mSolverVariable, constraintAnchor3.mSolverVariable, -constraintAnchor2.getMargin(), i3);
            }
        }
        if (z11) {
        }
        ConstraintAnchor constraintAnchor142 = constraintWidget8.mListAnchors[i2];
        int i232 = i2 + 1;
        ConstraintAnchor constraintAnchor152 = constraintWidget9.mListAnchors[i232];
        if (constraintAnchor142.mTarget == null) {
        }
        if (constraintAnchor152.mTarget == null) {
        }
        if (constraintWidget7 == constraintWidget9) {
        }
        if (constraintWidget8 == constraintWidget9) {
        }
        if (solverVariable5 != null) {
        }
    }
}
